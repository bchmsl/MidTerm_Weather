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
import com.bchmsl.midterm_weather.ui.login.LoginFragmentDirections
import com.bchmsl.midterm_weather.ui.signup.SignUpFragmentDirections
import com.google.firebase.auth.FirebaseAuth

class signUpContinueFragment : BaseFragment<FragmentSignUpContinueBinding>(FragmentSignUpContinueBinding::inflate) {
    // firebaseAuth
    private lateinit var firebaseAuth : FirebaseAuth
    override fun start() {
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()
        //get user info
        val firebaseUser  = firebaseAuth.currentUser
        val email = firebaseUser!!.email
        binding.apply {
            btnFinish.setOnClickListener {
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
            }
        }
    }
    private fun goToMainFra(){
        findNavController().navigate(signUpContinueFragmentDirections.actionSignUpContinueFragmentToMainFragment())
    }

}