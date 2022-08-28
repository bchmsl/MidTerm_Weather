package com.bchmsl.midterm_weather.ui.resetPassword

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentResetPasswordBinding
import com.bchmsl.midterm_weather.extensions.*
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.utils.ProcessingDialog
import com.bchmsl.midterm_weather.utils.ResponseHandler
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
                it.hideKeyboard()
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
                it.hideKeyboard()
                findNavController().popBackStack()
            }
            tilEmail.editText?.addTextChangedListener {
                tilEmail.isValidEmail()
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
        findNavController().popBackStack()
    }

    private fun showProcessBar() {
        processing.startProcessing()
    }

    private fun hideProcessBar() {
        processing.stopProcessing()
    }
}
