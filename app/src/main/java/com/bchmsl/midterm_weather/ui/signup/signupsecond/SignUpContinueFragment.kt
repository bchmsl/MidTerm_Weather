package com.bchmsl.midterm_weather.ui.signup.signupsecond

import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentSignUpContinueBinding
import com.bchmsl.midterm_weather.extensions.isFieldEmpty
import com.bchmsl.midterm_weather.extensions.makeErrorSnackbar
import com.bchmsl.midterm_weather.extensions.makeSuccessSnackbar
import com.bchmsl.midterm_weather.firebase.Firebase
import com.bchmsl.midterm_weather.model.User
import com.bchmsl.midterm_weather.network.utils.ResponseHandler
import com.bchmsl.midterm_weather.ui.ProcessingDialog
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class SignUpContinueFragment :
    BaseFragment<FragmentSignUpContinueBinding>(FragmentSignUpContinueBinding::inflate) {
    // firebaseAuth
    private val processing = ProcessingDialog(this)
    private var imageUri: Uri? = null
    private val uid: String? by lazy { Firebase.firebaseAuth.currentUser?.uid }

    private val viewModel: SignUpContinueViewModel by viewModels()
    override fun start() {
        listeners()
    }

    private fun listeners() {
        binding.apply {
            ibtnChoosePhoto.setOnClickListener {
                choosePhoto()
            }
            ibtnNext.setOnClickListener {
                if (checkFields()) {
                    uploadToDatabase()
                }
            }
        }
    }

    private fun choosePhoto() {
        ImagePicker.Companion.with(this@SignUpContinueFragment)
            .crop(150f, 150f)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private fun uploadToDatabase() {
        binding.apply {
            showProcessBar()
            val firstName = tilFirstName.editText?.text.toString()
            val lastName = tilLastName.editText?.text.toString()
            val user = User(capitalize(firstName), capitalize(lastName))
            if (uid != null) {
                viewModel.addToDatabase(uid!!, user)
                lifecycleScope.launch {
                    viewModel.addToDatabaseResponse.collect {
                        when (it) {
                            is ResponseHandler.Success<*> -> {
                                uploadProfilePic()
                            }
                            is ResponseHandler.Error -> {
                                handleError(Throwable("failed to add additional sign up data  ${it.error.message}"))
                            }
                            else -> {}
                        }
                    }
                }
            } else {
                hideProcessBar()
                handleError(Throwable("error: user ID is null"))
            }
        }
    }


    private fun uploadProfilePic() {
        viewModel.uploadProfilePic(uid, imageUri)
        lifecycleScope.launch {
            viewModel.uploadProfilePicResponse.collect {
                when (it) {
                    is ResponseHandler.Success<*> -> {
                        handleUploadProfilePicSuccess()
                    }
                    is ResponseHandler.Error -> {
                        handleError(Throwable("Failed to upload this image: ${it.error.message}"))
                    }
                    else -> {}
                }
            }
        }
    }

    private fun handleUploadProfilePicSuccess() {
        hideProcessBar()
        binding.root.makeSuccessSnackbar("Registration was successful")
        goToMainFra()
    }

    private fun handleError(error: Throwable) {
        hideProcessBar()
        binding.root.makeErrorSnackbar(error.message!!)
    }

    private fun checkFields(): Boolean {
        binding.apply {
            return when {
                tilFirstName.isFieldEmpty() || tilLastName.isFieldEmpty() -> false
                imageUri == null -> {
                    handleError(Throwable("Please upload an image"))
                    false
                }
                else -> true
            }
        }
    }

    private fun goToMainFra() {
        findNavController().navigate(SignUpContinueFragmentDirections.actionSignUpContinueFragmentToMainFragment())
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


                    Glide.with(this@SignUpContinueFragment)
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