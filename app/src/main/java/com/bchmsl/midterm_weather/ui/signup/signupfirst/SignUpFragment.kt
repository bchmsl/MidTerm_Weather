package com.bchmsl.midterm_weather.ui.signup.signupfirst

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentSignUpBinding
import com.bchmsl.midterm_weather.extensions.isFieldEmpty
import com.bchmsl.midterm_weather.extensions.isValidEmail
import com.bchmsl.midterm_weather.extensions.makeErrorSnackbar
import com.bchmsl.midterm_weather.extensions.notGoodPass
import com.bchmsl.midterm_weather.network.utils.ResponseHandler
import com.bchmsl.midterm_weather.ui.ProcessingDialog
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.google.firebase.auth.FirebaseUser
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
                //validate data
                if (checkFields()) {
                    val email = tilEmail.editText?.text.toString()
                    val password = tilPassword.editText?.text.toString()
                    firebaseSignUp(email, password)
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

    private fun firebaseSignUp(email: String, password: String) {
        //show progress bar
        showProcessBar()
        //create account
        viewModel.signupUser(email, password)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signUnResponse.collect {
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
        binding.root.makeErrorSnackbar("Sign up failed due to ${e.message}")
    }

    private fun handleSuccess(firebaseUser: FirebaseUser) {
        hideProcessBar()
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
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
    }
}