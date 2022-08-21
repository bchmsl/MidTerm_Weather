package com.bchmsl.midterm_weather

import android.app.Activity
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.databinding.FragmentSignUpContinueBinding
import com.bchmsl.midterm_weather.model.User
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.ui.signup.checkEmpty
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class signUpContinueFragment : BaseFragment<FragmentSignUpContinueBinding>(FragmentSignUpContinueBinding::inflate) {
    // firebaseAuth
    private lateinit var firebaseAuth : FirebaseAuth
    private  lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri
    var firstName = ""
    var lastName = ""
    var uid: String? = null
    override fun start() {
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()
        //get user info
        val firebaseUser  = firebaseAuth.currentUser
        //user id
        uid = firebaseUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        binding.apply {
            IWCamera.setOnClickListener {
                ImagePicker.Companion.with(this@signUpContinueFragment)
                    .crop(150f,150f)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }

            }
            btnFinish.setOnClickListener {
                when {
                    checkEmpty(tilFirstName) || checkEmpty(tilLastName) -> {}
                    else -> {
                        signUpContinueProgressBar.visibility = View.VISIBLE
                        firstName = tilFirstName.editText?.text.toString()
                        lastName = tilLastName.editText?.text.toString()
                        val user = User(firstName, lastName)
                        if(uid != null) {
                            databaseReference.child(uid!!).setValue(user).addOnSuccessListener {
                                //successful

                                //now uploading profile picture
                                uploadProfilePic()
                            }
                                .addOnFailureListener {
                                    // failed
                                    hideProgressBar()
                                    Toast.makeText(requireContext(), "failed to add additional sign up data  ${it.message}", Toast.LENGTH_SHORT ).show()
                                }

                        } else {
                            hideProgressBar()
                            Toast.makeText(requireContext(), "error: user ID is null", Toast.LENGTH_SHORT ).show()


                        }
                    }
                }
            }
        }
    }

    private fun uploadProfilePic() {
        storageReference = FirebaseStorage.getInstance().getReference("Users/$uid")
        storageReference.putFile(imageUri).addOnSuccessListener {
            hideProgressBar()
            Snackbar.make(binding.singUpContinueRootLayout, "Registration was successful", Snackbar.LENGTH_SHORT)
                .setTextMaxLines(1)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.lime))
                .show()
            goToMainFra()
        }.addOnFailureListener {
            hideProgressBar()
            Snackbar.make(binding.singUpContinueRootLayout, "Failed to upload this image: ${it.message}", Snackbar.LENGTH_SHORT)
                .setTextMaxLines(1)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.regular_red))
                .show()
        }
    }

    private fun goToMainFra(){
        findNavController().navigate(signUpContinueFragmentDirections.actionSignUpContinueFragmentToMainFragment())
    }

    private fun hideProgressBar() {
        binding.signUpContinueProgressBar.visibility = View.GONE
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


                    Glide.with(this@signUpContinueFragment)
                        .load(imageUri)
                        .into((binding.imageUser) as ImageView)

                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

}