package com.bchmsl.midterm_weather.ui.preferences

import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentPreferencesBinding
import com.bchmsl.midterm_weather.ui.base.BaseFragment

class PreferencesFragment : BaseFragment<FragmentPreferencesBinding>(FragmentPreferencesBinding::inflate) {
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
    }

}