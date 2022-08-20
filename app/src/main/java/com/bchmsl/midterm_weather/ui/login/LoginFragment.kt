package com.bchmsl.midterm_weather.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.databinding.FragmentLoginBinding
import com.bchmsl.midterm_weather.ui.base.BaseFragment

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    override fun start() {
        binding.btnLogIn.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLogInFragmentToMainFragment())
        }
        binding.btnContinueToSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLogInFragmentToSignUpFragment())
        }
    }

}