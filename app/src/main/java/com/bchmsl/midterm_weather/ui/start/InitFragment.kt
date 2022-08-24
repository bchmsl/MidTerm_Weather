package com.bchmsl.midterm_weather.ui.start

import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentInitBinding
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth

class InitFragment : BaseFragment<FragmentInitBinding>(FragmentInitBinding::inflate) {
    // firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    override fun start() {
        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
    }

    private fun checkUser() {
        //if user is already logged in
        //go to LoggedIn/profile/main activity

        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            //user is already logged in
            goToMainFra()
        } else {
            goToLogInFra()
        }
    }

    private fun goToMainFra() {
        findNavController().navigate(InitFragmentDirections.actionStartFragmentToMainFragment())
    }

    private fun goToLogInFra() {
        findNavController().navigate(InitFragmentDirections.actionStartFragmentToLogInFragment())
    }
}