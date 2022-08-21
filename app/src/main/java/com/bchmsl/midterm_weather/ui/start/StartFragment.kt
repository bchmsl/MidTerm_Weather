package com.bchmsl.midterm_weather.ui.start

import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentStartBinding
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth

class StartFragment : BaseFragment<FragmentStartBinding>(FragmentStartBinding::inflate) {
    // firebaseAuth
    private lateinit var firebaseAuth : FirebaseAuth
    override fun start() {
        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        binding.btnLogIn.setOnClickListener {
            goToLogInFra()
        }
        binding.btnSignUp.setOnClickListener {
            goToSingUpFra()
        }
    }
    private fun checkUser() {
        //if user is already logged in
        //go to LoggedIn/profile/main activity

        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser !=null){
            //user is already logged in
            goToMainFra()

        }
    }
    private fun goToMainFra(){
        findNavController().navigate(StartFragmentDirections.actionStartFragmentToMainFragment())
    }
    private fun goToSingUpFra(){
        findNavController().navigate(StartFragmentDirections.actionStartFragmentToSignUpFragment())
    }

    private fun goToLogInFra(){
        findNavController().navigate(StartFragmentDirections.actionStartFragmentToLogInFragment())
    }
}