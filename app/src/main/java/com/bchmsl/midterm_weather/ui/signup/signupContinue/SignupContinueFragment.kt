package com.bchmsl.midterm_weather.ui.signup.signupContinue

import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentSignupContinueBinding
import com.bchmsl.midterm_weather.extensions.*
import com.bchmsl.midterm_weather.models.User
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.utils.ProcessingDialog
import com.bchmsl.midterm_weather.utils.ResponseHandler
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.launch
import java.util.*

class SignupContinueFragment :
    BaseFragment<FragmentSignupContinueBinding>(FragmentSignupContinueBinding::inflate) {
    // firebaseAuth
    private val processing = ProcessingDialog(this)
    private var imageUri: Uri? = null
    private val viewModel: SignupContinueViewModel by viewModels()
    override fun start() {
        listeners()
    }

    private fun listeners() {
        binding.apply {
            ibtnChoosePhoto.setOnClickListener {
                it.hideKeyboard()
                choosePhoto()
            }
            ibtnNext.setOnClickListener {
                it.hideKeyboard()
                showProcessBar()
                if (checkFields()) {
                    uploadToDatabase()
                }
            }
            tilFirstName.editText?.addTextChangedListener {
                tilFirstName.isValidName()
            }
        }
    }

    private fun choosePhoto() {
        ImagePicker.Companion.with(this@SignupContinueFragment)
            .crop(150f, 150f)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private fun uploadToDatabase() {
        binding.apply {
            val firstName = tilFirstName.editText?.text.toString()
            val lastName = tilLastName.editText?.text.toString()
            val user = User(capitalize(firstName), capitalize(lastName))
            viewModel.addToDatabase(user)
            lifecycleScope.launch {
                viewModel.addToDatabaseResponse.collect {
                    when (it) {
                        is ResponseHandler.Success<*> -> {
                            if(imageUri != null)
                            uploadProfilePic()
                            else handleAdditionalInfoSuccess()
                        }
                        is ResponseHandler.Error -> {
                            handleError(Throwable("failed to add additional sign up data  ${it.error.message}"))
                        }
                        else -> {}
                    }
                }
            }

        }
    }


    private fun uploadProfilePic() {
        viewModel.uploadProfilePic(imageUri)
        lifecycleScope.launch {
            viewModel.uploadProfilePicResponse.collect {
                when (it) {
                    is ResponseHandler.Success<*> -> {
                        handleAdditionalInfoSuccess()
                    }
                    is ResponseHandler.Error -> {
                        handleError(Throwable("Failed to upload this image: ${it.error.message}"))
                    }
                    else -> {}
                }
            }
        }
    }

    private fun handleAdditionalInfoSuccess() {
        hideProcessBar()
        binding.root.makeSuccessSnackbar("Additional info was added successfully")
        goToMainFra()
    }

    private fun handleError(error: Throwable) {
        hideProcessBar()
        binding.root.makeErrorSnackbar(error.message!!)
    }

    private fun checkFields(): Boolean {
        binding.apply {
            return when {
                tilFirstName.isFieldEmpty() || tilLastName.isFieldEmpty() -> {
                    hideProcessBar()
                    false
                }
                !tilFirstName.isValidName() -> {
                    hideProcessBar()
                    false
                }
                else -> true
            }
        }
    }

    private fun goToMainFra() {
        findNavController().navigate(SignupContinueFragmentDirections.actionSignUpContinueFragmentToMainFragment())
    }

    private fun showProcessBar() {
        processing.startProcessing()
    }

    private fun hideProcessBar() {
        processing.stopProcessing()
    }

    private fun capitalize(str: String): String {
        return str.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    //                 viewModel.updateUri(data?.data!!)
                    imageUri = data?.data!!


                    Glide.with(this@SignupContinueFragment)
                        .load(imageUri)
                        .into((binding.imageUser) as ImageView)

                }
                ImagePicker.RESULT_ERROR -> {
                    binding.root.makeErrorSnackbar(ImagePicker.getError(data))
                }
                else -> {
                    binding.root.makeErrorSnackbar("Task Cancelled")
                }
            }
        }

}