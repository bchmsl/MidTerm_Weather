package com.bchmsl.midterm_weather.ui.preferences.changeinfo

import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentChangeUserInfoBinding
import com.bchmsl.midterm_weather.ui.base.BaseFragment

class ChangeUserInfoFragment :
    BaseFragment<FragmentChangeUserInfoBinding>(FragmentChangeUserInfoBinding::inflate) {
    override fun start() {
        listeners()

    }

    private fun listeners() {
        binding.tvProfileInfoTitle.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}