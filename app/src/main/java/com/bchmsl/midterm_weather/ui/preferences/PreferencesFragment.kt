package com.bchmsl.midterm_weather.ui.preferences

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentPreferencesBinding
import com.bchmsl.midterm_weather.ui.base.BaseFragment

class PreferencesFragment :
    BaseFragment<FragmentPreferencesBinding>(FragmentPreferencesBinding::inflate) {
    private val viewModel: PreferencesViewModel by viewModels()
    override fun start() {
        listeners()
    }

    private fun listeners() {
        binding.llChangeCity.setOnClickListener {
            findNavController().navigate(PreferencesFragmentDirections.actionPreferencesFragmentToCityChangeFragment())
        }
        binding.tvPreferencesTitle.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.llChangeUserDetails.setOnClickListener {
            findNavController().navigate(PreferencesFragmentDirections.actionPreferencesFragmentToChangeUserInfoFragment())
        }
        binding.llLogOut.setOnClickListener {
            //init firebaseAuth
            signOut()
        }
    }

    private fun signOut() {
        viewModel.signOut()
        findNavController().navigate(PreferencesFragmentDirections.actionPreferencesFragmentToLogInFragment())

    }

}