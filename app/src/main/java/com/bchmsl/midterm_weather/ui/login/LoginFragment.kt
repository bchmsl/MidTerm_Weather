package com.bchmsl.midterm_weather.ui.login

import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.databinding.FragmentLoginBinding
import com.bchmsl.midterm_weather.ui.ProcessingDialog
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.ui.signup.signupfirst.isValidEmail
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
            if(!isValidEmail(binding.tilEmail)) {}
            else {
                //data is validated, begin login
                firebaseLogin()
            }
        }
        binding.tvSignUp.setOnClickListener {
            goToSingUpFra()
        }
    }

    private fun firebaseLogin() {
        //show progress bar
        showProcessBar()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //login success

                //hide progress bar
                hideProcessBar()

                //go to LoggedIn/profile Fragment
                goToMainFra()


            }
            .addOnFailureListener{ e->
                //login failed

                //hide progress bar
                hideProcessBar()
                makeSnackBar("Login failed due to ${e.message}")
            }
    }

    private fun makeSnackBar(message: String){
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setTextMaxLines(2)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.regular_red))
            .show()
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

}