package com.bchmsl.midterm_weather.ui.preferences.changeUserInfo

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.databinding.FragmentChangeUserInfoBinding
import com.bchmsl.midterm_weather.extensions.*
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.utils.ResponseHandler
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.launch
import java.io.File

class UserInfoChangeFragment :
    BaseFragment<FragmentChangeUserInfoBinding>(FragmentChangeUserInfoBinding::inflate) {
    private val args by navArgs<UserInfoChangeFragmentArgs>()
    private val viewModel: UserInfoChangeViewModel by viewModels()
    private var imageUri: Uri? = null
    override fun start() {
        getUserInfo()
        checkEmailVerification()
        checkIfEditing()
        listeners()
    }

    private fun checkIfEditing() {
        val isChanging = args.isChanging
        binding.apply {
            tilFirstName.editText?.isEnabled = isChanging
            tilLastName.editText?.isEnabled = isChanging
            tvFinishedEditing.isVisible = isChanging
            btnSave.isVisible = isChanging
            btnCamera.isVisible = isChanging
            btnVerify.isVisible = isChanging
        }
        if (!isChanging) {
            binding.tvProfileInfoTitle.text = getString(R.string.profile_info)
        }

    }

    private fun checkEmailVerification() {
        if (viewModel.isEmailVerified() == true) {
            binding.EmailVerStatus.text = getString(R.string.emailVerStatus, "Verified")
        } else {
            binding.EmailVerStatus.text = getString(R.string.emailVerStatus, "Not verified")
            binding.btnVerify.visibility = View.VISIBLE
        }
        binding.EmailVerStatus.visibility = View.VISIBLE
    }

    private fun getUserInfo() {
        //show loading bar
        binding.lpiLoading.visibility = View.VISIBLE
        //disable save button while loading is happening
        disableSaveButton()
        //user id
        viewModel.getUserInfo()
        lifecycleScope.launch {
            viewModel.userInfoResponse.collect {
                when (it) {
                    is ResponseHandler.Success<*> -> {
                        handleUserInfoSuccess(it.data as DataSnapshot)
                    }
                    is ResponseHandler.Error -> {
                        viewModel.addEmptyPlaceHoldersToDatabase()
                    }
                    else -> {}
                }
            }
        }

    }

    private fun handleUserInfoSuccess(dataSnapshot: DataSnapshot) {
        if (dataSnapshot.exists()) {
            val firstName = (dataSnapshot.value as HashMap<*, *>)["firstName"].toString()
            val lastName = (dataSnapshot.value as HashMap<*, *>)["lastName"].toString()
            //get image
            val localFile = File.createTempFile("tempFile", "jpg")
            viewModel.getUserImage(localFile)
            lifecycleScope.launch {
                viewModel.userImageResponse.collect {
                    when (it) {
                        is ResponseHandler.Success<*> -> {
                            handleUserImagePart(localFile, firstName, lastName)
                        }
                        is ResponseHandler.Error -> {
//                            handleError(Throwable("you have no profile picture uploaded"))
                            handleUserImagePart(null ,firstName, lastName)
                            hideLoading()
                        }
                        else -> {}
                    }
                }
            }
        } else {
            handleError(Throwable("No logged in User's info was found"))
        }
    }

    private fun handleUserImagePart(localFile: File?, firstName: String, lastName: String) {
        binding.apply {
            tilFirstName.editText?.setText(firstName)
            tilLastName.editText?.setText(lastName)
            if(localFile!=null) {
                val bitmap: Bitmap =
                    BitmapFactory.decodeFile(localFile.absolutePath)
                imageUser.setImageBitmap(bitmap)
            }
        }
        hideLoading()
        //enable save button after loading finished
        enableSaveButton()
    }

    private fun disableSaveButton() {
        binding.apply {
            btnSave.isEnabled = false
            btnSave.isClickable = false
        }
    }

    private fun enableSaveButton() {
        binding.apply {
            btnSave.isEnabled = true
            btnSave.isClickable = true
        }
    }

    private fun hideLoading() {
        binding.lpiLoading.visibility = View.GONE
    }

    private fun listeners() {
        binding.tvProfileInfoTitle.setOnClickListener {
            it.hideKeyboard()
            findNavController().popBackStack()
        }
        binding.btnSave.setOnClickListener {
            it.hideKeyboard()
            updateInfo()
        }
        binding.btnCamera.setOnClickListener {
            it.hideKeyboard()
            ImagePicker.Companion.with(this@UserInfoChangeFragment)
                .crop(150f, 150f)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }

        }
        binding.btnVerify.setOnClickListener {
            it.hideKeyboard()
            sendVerification()
            signOut()
        }
    }

    private fun signOut() {
        viewModel.signOut()
        goToLoginFra()
    }

    private fun sendVerification() {
        viewModel.sendEmailVerification()
        binding.root.makeSnackbar("Verification email was sent")
    }

    private fun updateInfo() {
        binding.apply {
            viewModel.updateUserInfo(
                tilFirstName.editText?.text.toString().capitalizeFirstChar(),
                tilLastName.editText?.text.toString().capitalizeFirstChar(),
                imageUri
            )
            root.makeSuccessSnackbar("Success")
            goToMainFra()
        }
    }

    private fun handleError(error: Throwable) {
        binding.root.makeSnackbar(error.message.toString())
        hideLoading()
    }

    private fun goToMainFra() {
        findNavController().navigate(UserInfoChangeFragmentDirections.actionChangeUserInfoFragmentToMainFragment())
    }

    private fun goToLoginFra() {
        findNavController().navigate(UserInfoChangeFragmentDirections.actionChangeUserInfoFragmentToLogInFragment())
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


                    Glide.with(this@UserInfoChangeFragment)
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