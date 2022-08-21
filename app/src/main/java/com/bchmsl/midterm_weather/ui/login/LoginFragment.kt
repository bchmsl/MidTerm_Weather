package com.bchmsl.midterm_weather.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.databinding.FragmentLoginBinding
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.ui.signup.SignUpFragmentDirections
import com.bchmsl.midterm_weather.ui.signup.isValidEmail
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    // firebaseAuth
    private lateinit var firebaseAuth : FirebaseAuth
    private var email = ""
    private var password = ""

    override fun start() {
        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnLogIn.setOnClickListener {
            //get data
             email = binding.tilEmail.editText?.text.toString()
             password = binding.tilPassword.editText?.text.toString()
            //validate data
            if(!isValidEmail(binding.tilEmail)) {}
            else {
                //data is validated, begin login
                firebaseLogin()
            }
        }
        binding.btnContinueToSignUp.setOnClickListener {
            goToSingUpFra()
        }
    }

    private fun firebaseLogin() {
        //show progress bar
        binding.loginProgressBar.visibility = View.VISIBLE
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //login success

                //hide progress bar
                hideProgressBar()

                //get user info
                val firebaseUser  = firebaseAuth.currentUser
                val email = firebaseUser!!.email
//                Toast.makeText(this, "Logged in as $email", Toast.LENGTH_SHORT).show()

                //go to LoggedIn/profile Fragment
                goToMainFra()


            }
            .addOnFailureListener{ e->
                //login failed

                //hide progress bar
                hideProgressBar()
                Toast.makeText(requireContext(), "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()


            }
    }

    private fun hideProgressBar() {
        binding.loginProgressBar.visibility = View.GONE
    }

    private fun goToSingUpFra(){
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignUpContinueFragment())
    }

    private fun goToMainFra(){
        findNavController().navigate(LoginFragmentDirections.actionLogInFragmentToMainFragment())
    }

}