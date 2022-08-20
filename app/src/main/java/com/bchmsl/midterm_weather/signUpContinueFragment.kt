package com.bchmsl.midterm_weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentLoginBinding
import com.bchmsl.midterm_weather.databinding.FragmentSignUpContinueBinding
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.ui.signup.SignUpFragmentDirections

class signUpContinueFragment : BaseFragment<FragmentSignUpContinueBinding>(FragmentSignUpContinueBinding::inflate) {
    override fun start() {
        binding.btnFinish.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
        }
    }


}