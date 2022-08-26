package com.bchmsl.midterm_weather.ui.login

import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.databinding.FragmentLoginBinding
import com.bchmsl.midterm_weather.extensions.checkEmpty
import com.bchmsl.midterm_weather.extensions.isValidEmail
import com.bchmsl.midterm_weather.extensions.makeErrorSnackbar
import com.bchmsl.midterm_weather.extensions.makeSnackbar
import com.bchmsl.midterm_weather.ui.ProcessingDialog
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    // firebaseAuth
    private lateinit var firebaseAuth : FirebaseAuth
    private val processing = ProcessingDialog(this)
    private var email = ""
    private var password = ""

    override fun start() {
        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        listeners()
    }

    private fun listeners() {
        binding.ibtnNext.setOnClickListener {
            //get data
            email = binding.tilEmail.editText?.text.toString()
            password = binding.tilPassword.editText?.text.toString()
            //validate data
            when {
                !binding.tilEmail.isValidEmail() -> {}
                binding.tilPassword.checkEmpty() -> {}
                else -> {
                    //data is validated, begin login
                    firebaseLogin()
                }
            }
        }
        binding.tvSignUp.setOnClickListener {
            goToSingUpFra()
        }
        binding.tvForgotPassword.setOnClickListener {
            goToResetPassFra()
        }
    }

    private fun firebaseLogin() {
        //show progress bar
        showProcessBar()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //login success
                val firebaseUser = firebaseAuth.currentUser
                if(!firebaseUser!!.isEmailVerified){
                binding.root.makeSnackbar("Please Validate Email")
                }
                    //hide progress bar
                    hideProcessBar()

                    //go to LoggedIn/profile Fragment
                    goToMainFra()

            }
            .addOnFailureListener{ e->
                //login failed

                //hide progress bar
                hideProcessBar()
                binding.root.makeErrorSnackbar("Login failed due to ${e.message}")
            }
    }



    private fun showProcessBar() {
        processing.startProcessing()
    }

    private fun hideProcessBar() {
        processing.stopProcessing()
    }

    private fun goToSingUpFra(){
        findNavController().navigate(LoginFragmentDirections.actionLogInFragmentToSignUpFragment())
    }

    private fun goToMainFra(){
        findNavController().navigate(LoginFragmentDirections.actionLogInFragmentToMainFragment())
    }

    private fun goToResetPassFra() {
        findNavController().navigate(LoginFragmentDirections.actionLogInFragmentToResetPasswordFragment())
    }

}