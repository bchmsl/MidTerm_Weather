package com.bchmsl.midterm_weather.ui.signup.signupfirst

import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentSignUpBinding
import com.bchmsl.midterm_weather.extensions.checkEmpty
import com.bchmsl.midterm_weather.extensions.isValidEmail
import com.bchmsl.midterm_weather.extensions.makeErrorSnackbar
import com.bchmsl.midterm_weather.extensions.notGoodPass
import com.bchmsl.midterm_weather.ui.ProcessingDialog
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private val processing = ProcessingDialog(this)
    private var email = ""
    private var password = ""
    override fun start() {
        firebaseAuth = FirebaseAuth.getInstance()
        listeners()


    }

    private fun listeners() {
        binding.apply {
            ibtnNext.setOnClickListener {
                //validate data
                when {
                    tilEmail.checkEmpty() || tilPassword.checkEmpty() || tilRepeatPassword.checkEmpty() -> {
                    }
                    tilPassword.notGoodPass() -> {}
                    !tilEmail.isValidEmail() -> {}
                    tilPassword.editText?.text.toString() != tilRepeatPassword.editText?.text.toString() -> {
                        binding.root.makeErrorSnackbar("Passwords should match")
                    }
                    else -> {
                        //data is validated, continue signup
                        //get data
                        email = binding.tilEmail.editText?.text.toString()
                        password = binding.tilPassword.editText?.text.toString()
                        firebaseSignUp()
                    }
                }

            }
            tvSignIn.setOnClickListener {
                goToLogInFra()
            }
            ibtnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun firebaseSignUp() {
        //show progress bar
        showProcessBar()
        //create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //sing up success

                //hide progress bar
                hideProcessBar()

                //get current use
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
//                Toast.makeText(requireContext(), "Almost done", Toast.LENGTH_SHORT ).show()
                //go to fragment of second part of registration
                goToSingUpContinueFra()

            }
            .addOnFailureListener { e ->
                //sing up failed

                //hide progress bar
                hideProcessBar()

                binding.root.makeErrorSnackbar("Sign up failed due to ${e.message}")
            }
    }

    private fun showProcessBar() {
        processing.startProcessing()
    }

    private fun hideProcessBar() {
        processing.stopProcessing()
    }


    private fun goToSingUpContinueFra() {
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignUpContinueFragment())
    }

    private fun goToLogInFra() {
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
    }


}