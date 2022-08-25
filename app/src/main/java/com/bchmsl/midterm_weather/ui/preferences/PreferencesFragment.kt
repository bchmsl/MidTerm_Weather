package com.bchmsl.midterm_weather.ui.preferences

import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentPreferencesBinding
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth

class PreferencesFragment : BaseFragment<FragmentPreferencesBinding>(FragmentPreferencesBinding::inflate) {
    private lateinit var firebaseAuth: FirebaseAuth
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
            firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signOut()
            findNavController().navigate(PreferencesFragmentDirections.actionPreferencesFragmentToLogInFragment())

        }
    }

}