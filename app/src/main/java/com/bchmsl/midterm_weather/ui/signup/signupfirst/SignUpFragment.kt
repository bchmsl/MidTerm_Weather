package com.bchmsl.midterm_weather.ui.signup.signupfirst

import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.databinding.FragmentSignUpBinding
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


fun checkEmpty(Etext: TextInputLayout): Boolean {
    return if (Etext.editText?.text.toString().trim().isEmpty()) {
        Etext.helperText = "Required"
        true
    } else {
        Etext.helperText = ""
        false
    }
}

fun isValidEmail(Etext: TextInputLayout): Boolean {
    val email = Etext.editText?.text.toString().trim()
    return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        Etext.helperText = "invalid email"
        false
    } else {
        Etext.helperText = ""
        true
    }
}

fun isValidUsername(Etext: TextInputLayout): Boolean {
    val username = Etext.editText?.text.toString().trim()
    return if (username.length < 10) {
        Etext.helperText = "username should be at least 10 characters"
        false
    } else {
        Etext.helperText = ""
        true
    }

}

fun notGoodPass(Epassword: TextInputLayout): Boolean {
    val password = Epassword.editText?.text.toString()
    when {
        password.length <= 8 -> {
            Epassword.helperText = "Password should be more than 8 characters"
            return true
        }
        password.contains(Regex("^[0-9]+[^a-zA-z]*$")) -> {
            Epassword.helperText = "Password should contain at least one alphabet character"
            return true
        }
        password.contains(Regex("^[a-zA-Z]+[^0-9]*$")) -> {
            Epassword.helperText = "Password should contain at least one numeric character"
            return true
        }
        else -> {
            Epassword.helperText = ""
            return false
        }
    }
}

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""
    override fun start() {
        firebaseAuth = FirebaseAuth.getInstance()
        binding.apply {
            ibtnNext.setOnClickListener {
                //validate data
                when {
                    checkEmpty(tilEmail) || checkEmpty(tilPassword) || checkEmpty(
                        tilRepeatPassword
                    ) -> { }
                    notGoodPass(tilPassword) -> {}
                    !isValidEmail(tilEmail) -> {}
                    tilPassword.editText?.text.toString()!=tilRepeatPassword.editText?.text.toString() -> {
                        Snackbar.make(binding.root, "Passwords should match", Snackbar.LENGTH_SHORT)
                            .setTextMaxLines(1)
                            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.regular_red))
                            .show()
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
        }


    }

    private fun firebaseSignUp() {
        //show progress bar
        binding.pbSignup.visibility = View.VISIBLE
        //create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //sing up success

                //hide progress bar
                hideProgressBar()

                //get current use
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
//                Toast.makeText(requireContext(), "Almost done", Toast.LENGTH_SHORT ).show()
                //go to fragment of second part of registration
                goToSingUpContinueFra()

            }
            .addOnFailureListener{e->
                //sing up failed

                //hide progress bar
                hideProgressBar()

                Snackbar.make(binding.root, "Sign up failed due to ${e.message}", Snackbar.LENGTH_SHORT)
                    .setTextMaxLines(1)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.regular_red))
                    .show()
            }
    }
    private fun hideProgressBar() {
        binding.pbSignup.visibility = View.GONE
    }

    private fun goToSingUpContinueFra(){
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignUpContinueFragment())
    }

    private fun goToLogInFra(){
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
    }


}