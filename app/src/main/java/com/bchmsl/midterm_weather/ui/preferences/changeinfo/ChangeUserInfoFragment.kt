package com.bchmsl.midterm_weather.ui.preferences.changeinfo

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.databinding.FragmentChangeUserInfoBinding
import com.bchmsl.midterm_weather.extensions.makeErrorSnackbar
import com.bchmsl.midterm_weather.extensions.makeSnackbar
import com.bchmsl.midterm_weather.extensions.makeSuccessSnackbar
import com.bchmsl.midterm_weather.ui.ProcessingDialog
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.ui.login.LoginFragmentDirections
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class ChangeUserInfoFragment :
    BaseFragment<FragmentChangeUserInfoBinding>(FragmentChangeUserInfoBinding::inflate) {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private val processing = ProcessingDialog(this)
    private var imageUri: Uri? = null
    private var firstName = ""
    private var lastName = ""
    private var uid: String? = null
    override fun start() {
        getUserInfo()
        listeners()

    }

    private fun getUserInfo() {
        //show loading bar
        binding.lpiLoading.visibility = View.VISIBLE
        //disable save button while loading is happening
        disableSaveButton()
        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        //get current user main info
        val firebaseUser = firebaseAuth.currentUser
        //user id
        uid = firebaseUser?.uid
        if (uid != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            databaseReference.child(uid!!).get().addOnSuccessListener { it ->
                if (it.exists()) {
                    firstName = (it.value as HashMap<*, *>)["firstName"].toString()
                    lastName = (it.value as HashMap<*, *>)["lastName"].toString()
                    //get image
                    storageReference = FirebaseStorage.getInstance().getReference("Users/$uid")
                    val localFile: File = File.createTempFile("tempFile", "jpg")
                    storageReference.getFile(localFile).addOnSuccessListener {
                        binding.apply {
                            firstNameUser.setText(firstName)
                            lastNameUser.setText(lastName)
                            val bitmap: Bitmap =
                                BitmapFactory.decodeFile(localFile.absolutePath)
                            imageUser.setImageBitmap(bitmap)
                        }
                        hideLoading()
                        //enable save button after loading finished
                        enableSaveButton()
                    }.addOnFailureListener { exception ->
                        handleError("image:" + exception.message!!)
                        hideLoading()
                    }

                } else {
                    handleError("No logged in User's info was found")
                    //hide loading bar
                    hideLoading()
                }
            }
        }

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
            findNavController().popBackStack()
        }
        binding.btnSave.setOnClickListener {
            updateInfo()
        }
        binding.btnCamera.setOnClickListener {
            ImagePicker.Companion.with(this@ChangeUserInfoFragment)
                .crop(150f, 150f)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }

        }
    }

    private fun updateInfo() {
        binding.apply {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            val thisUserInfo = databaseReference.child(uid!!)
            thisUserInfo.child("firstName").setValue(capitalize(firstNameUser.text.toString()))
            thisUserInfo.child("lastName").setValue(capitalize(lastNameUser.text.toString()))
            storageReference = FirebaseStorage.getInstance().getReference("Users/$uid")
            if (imageUri != null) storageReference.putFile(imageUri!!)
            root.makeSuccessSnackbar("Success")
            goToMainFra()

        }
    }

    private fun handleError(error: String) {
        binding.root.makeSnackbar(error)
    }

    private fun goToMainFra() {
        findNavController().navigate(ChangeUserInfoFragmentDirections.actionChangeUserInfoFragmentToMainFragment())
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


                    Glide.with(this@ChangeUserInfoFragment)
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