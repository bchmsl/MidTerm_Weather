package com.bchmsl.midterm_weather.ui.reset_password

import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentResetPasswordBinding
import com.bchmsl.midterm_weather.extensions.checkEmpty
import com.bchmsl.midterm_weather.extensions.isValidEmail
import com.bchmsl.midterm_weather.extensions.makeErrorSnackbar
import com.bchmsl.midterm_weather.extensions.makeSuccessSnackbar
import com.bchmsl.midterm_weather.ui.ProcessingDialog
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordFragment :
    BaseFragment<FragmentResetPasswordBinding>(FragmentResetPasswordBinding::inflate) {
    // firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private val processing = ProcessingDialog(this)
    override fun start() {
        listeners()
    }

    private fun listeners() {
        binding.apply {
            ibtnNext.setOnClickListener {
                val email: String = tilEmail.editText!!.text.toString()
                when {
                    tilEmail.checkEmpty() -> {}
                    !tilEmail.isValidEmail() -> {}
                    else -> {
                        resetPassword(email)
                    }
                }
            }
        }
    }

    private fun resetPassword(email: String) {
        showProcessBar()

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            hideProcessBar()
            makeSnackBar(false, "Email sent to reset your password")
            goToLogInFra()
        }.addOnFailureListener {
            hideProcessBar()
            makeSnackBar(true, "failure due to ${it.message}")
        }
    }

    private fun goToLogInFra() {
        findNavController().navigate(ResetPasswordFragmentDirections.actionResetPasswordFragmentToLogInFragment())
    }

    private fun showProcessBar() {
        processing.startProcessing()
    }

    private fun hideProcessBar() {
        processing.stopProcessing()
    }

    private fun makeSnackBar(isFailure: Boolean, message: String) {
        if (isFailure) {
            binding.root.makeErrorSnackbar(message)
        } else {
            binding.root.makeSuccessSnackbar(message)
        }
    }
}