package com.bchmsl.midterm_weather.ui.reset_password

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentResetPasswordBinding
import com.bchmsl.midterm_weather.extensions.isFieldEmpty
import com.bchmsl.midterm_weather.extensions.isValidEmail
import com.bchmsl.midterm_weather.extensions.makeErrorSnackbar
import com.bchmsl.midterm_weather.extensions.makeSuccessSnackbar
import com.bchmsl.midterm_weather.network.utils.ResponseHandler
import com.bchmsl.midterm_weather.ui.ProcessingDialog
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import kotlinx.coroutines.launch

class ResetPasswordFragment :
    BaseFragment<FragmentResetPasswordBinding>(FragmentResetPasswordBinding::inflate) {
    // firebaseAuth
    private val viewModel: ResetPasswordViewModel by viewModels()
    private val processing = ProcessingDialog(this)
    override fun start() {
        listeners()
    }

    private fun listeners() {
        binding.apply {
            ibtnNext.setOnClickListener {
                val email: String = tilEmail.editText!!.text.toString()
                when {
                    tilEmail.isFieldEmpty() -> {}
                    !tilEmail.isValidEmail() -> {}
                    else -> {
                        resetPassword(email)
                    }
                }
            }
            ibtnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun resetPassword(email: String) {
        showProcessBar()
        viewModel.resetPassword(email)
        lifecycleScope.launch {
            viewModel.resetPasswordResponse.collect {
                when (it) {
                    is ResponseHandler.Success<*> -> {
                        handleSuccess()
                    }
                    is ResponseHandler.Error -> {
                        handleError(it.error)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun handleSuccess() {
        hideProcessBar()
        binding.root.makeSuccessSnackbar("Email sent to reset your password")
        goToLogInFra()
    }

    private fun handleError(e: Throwable) {
        hideProcessBar()
        binding.root.makeErrorSnackbar("failure due to ${e.message}")
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
}
