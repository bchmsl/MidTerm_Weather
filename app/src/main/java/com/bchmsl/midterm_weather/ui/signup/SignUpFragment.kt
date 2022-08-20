package com.bchmsl.midterm_weather.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.databinding.FragmentSignUpBinding
import com.bchmsl.midterm_weather.ui.base.BaseFragment

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {
    override fun start() {
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToMainFragment())
        }
        binding.btnContinueToLogIn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
        }
    }

}