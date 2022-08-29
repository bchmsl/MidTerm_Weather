package com.bchmsl.midterm_weather.ui.signup.signup

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentSignUpBinding
import com.bchmsl.midterm_weather.extensions.*
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.utils.ProcessingDialog
import com.bchmsl.midterm_weather.utils.ResponseHandler
import kotlinx.coroutines.launch

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {
    //FirebaseAuth
    val viewModel: SignUpViewModel by viewModels()
    private val processing = ProcessingDialog(this)
    override fun start() {
        listeners()
    }

    private fun listeners() {
        binding.apply {
            ibtnNext.setOnClickListener {
                it.hideKeyboard()
                //validate data
                if (checkFields()) {
                    val email = tilEmail.editText?.text.toString()
                    val password = tilPassword.editText?.text.toString()
                    firebaseSignUp(email, password)
                }

            }
            tvSignIn.setOnClickListener {
                it.hideKeyboard()
                goToLogInFra()
            }
            ibtnBack.setOnClickListener {
                it.hideKeyboard()
                findNavController().popBackStack()
            }
            tilPassword.editText?.addTextChangedListener {
                tilPassword.notGoodPass()
            }
            tilEmail.editText?.addTextChangedListener {
                tilEmail.isValidEmail()
            }
        }
    }

    private fun firebaseSignUp(email: String, password: String) {
        //show progress bar
        showProcessBar()
        //create account
        viewModel.signupUser(email, password)
        viewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signUnResponse.collect {
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
    }

    private fun handleError(e: Throwable) {
        hideProcessBar()
        binding.root.makeErrorSnackbar("Sign up failed due to ${e.message}")
    }

    private fun handleSuccess() {
        hideProcessBar()
        binding.root.makeSuccessSnackbar("Account created successfully!")
        goToSingUpContinueFra()
    }

    private fun checkFields(): Boolean {
        binding.apply {
            return when {
                tilEmail.isFieldEmpty() || tilPassword.isFieldEmpty() || tilRepeatPassword.isFieldEmpty() -> false
                tilPassword.notGoodPass() -> false
                !tilEmail.isValidEmail() -> false
                tilPassword.editText?.text.toString() != tilRepeatPassword.editText?.text.toString() -> {
                    binding.root.makeErrorSnackbar("Passwords should match")
                    false
                }
                else -> {
                    //data is validated, continue signup
                    //get data
                    true
                }
            }
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
        findNavController().popBackStack()
    }
}