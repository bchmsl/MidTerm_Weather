package com.bchmsl.midterm_weather.ui.preferences.mainPreferences

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentPreferencesBinding
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


class PreferencesFragment :
    BaseFragment<FragmentPreferencesBinding>(FragmentPreferencesBinding::inflate) {
    private val viewModel: PreferencesViewModel by viewModels()
    override fun start() {
        listeners()
        setSwitch()
    }

    private fun setSwitch() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getCurrentUnit().firstOrNull()?.let {
                    binding.swChangeUnits.isChecked = it
                }
            }
        }
    }

    private fun listeners() {
        binding.btnChangeCity.setOnClickListener {
            findNavController().navigate(PreferencesFragmentDirections.actionPreferencesFragmentToCityChangeFragment())
        }
        binding.tvPreferencesTitle.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(PreferencesFragmentDirections.actionPreferencesFragmentToChangeUserInfoFragment())
        }
        binding.btnLogOut.setOnClickListener {
            //init firebaseAuth
            signOut()
        }
        binding.btnAboutApp.setOnClickListener {
            makeDialog()
        }
        binding.swChangeUnits.setOnCheckedChangeListener { _, isEnabled ->
            viewModel.setUnit(isEnabled)
        }
    }

    private fun makeDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("About App")
        val info = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
        builder.setMessage("This app is made by Bachana Mosulishvili and Tengiz Gachechiladze.\n Version: ${info.versionName}")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun signOut() {
        viewModel.signOut()
        findNavController().navigate(PreferencesFragmentDirections.actionPreferencesFragmentToLogInFragment())
    }

}