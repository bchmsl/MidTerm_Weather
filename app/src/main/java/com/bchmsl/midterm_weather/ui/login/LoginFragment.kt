package com.bchmsl.midterm_weather.ui.login

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentLoginBinding
import com.bchmsl.midterm_weather.extensions.*
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.utils.ProcessingDialog
import com.bchmsl.midterm_weather.utils.ResponseHandler
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    // firebaseAuth
    private val viewModel: LoginViewModel by viewModels()
    private val processing = ProcessingDialog(this)

    override fun start() {
        //init firebaseAuth
        listeners()
    }

    private fun listeners() {
        binding.apply {
            ibtnNext.setOnClickListener {
                //get data
                it.hideKeyboard()
                val email = tilEmail.editText?.text.toString()
                val password = tilPassword.editText?.text.toString()
                //validate data
                if (checkFields()) {
                    firebaseLogin(email, password)
                }
            }
            tvSignUp.setOnClickListener {
                it.hideKeyboard()
                goToSignUpFra()
            }
            tvForgotPassword.setOnClickListener {
                it.hideKeyboard()
                goToResetPassFra()
            }
            tilEmail.editText?.addTextChangedListener {
                tilEmail.isValidEmail()
            }
        }
    }

    private fun firebaseLogin(email: String, password: String) {
        //show progress bar
        showProcessBar()
        viewModel.loginUser(email, password)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signInResponse.collect {
                    when (it) {
                        is ResponseHandler.Success<*> -> {
                            handleSuccess(it.data as FirebaseUser)
                        }
                        is ResponseHandler.Error -> {
                            handleError(it.error)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun handleError(e: Throwable) {
        hideProcessBar()
        binding.root.makeErrorSnackbar("Login failed due to ${e.message}")
    }

    private fun handleSuccess(user: FirebaseUser?) {
        hideProcessBar()
        if (!user!!.isEmailVerified) {
            binding.root.makeSnackbar("Please Validate Email")
        }
        goToMainFra()
    }

    private fun checkFields(): Boolean {
        return when {
            !binding.tilEmail.isValidEmail() -> false
            binding.tilPassword.isFieldEmpty() -> false
            else -> true
        }
    }


    private fun showProcessBar() {
        processing.startProcessing()
    }

    private fun hideProcessBar() {
        processing.stopProcessing()
    }

    private fun goToSignUpFra() {

        findNavController().navigate(LoginFragmentDirections.actionLogInFragmentToSignUpFragment())
    }

    private fun goToMainFra() {
        findNavController().navigate(LoginFragmentDirections.actionLogInFragmentToMainFragment())
    }

    private fun goToResetPassFra() {
        findNavController().navigate(LoginFragmentDirections.actionLogInFragmentToResetPasswordFragment())
    }

}