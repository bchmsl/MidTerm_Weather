package com.bchmsl.midterm_weather.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.databinding.FragmentStartBinding
import com.bchmsl.midterm_weather.ui.base.BaseFragment

class StartFragment : BaseFragment<FragmentStartBinding>(FragmentStartBinding::inflate) {
    override fun start() {
        binding.btnLogIn.setOnClickListener {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToLogInFragment())
        }
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToSignUpFragment())
        }
    }
}