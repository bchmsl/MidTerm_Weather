package com.bchmsl.midterm_weather.ui.signup.signupsecond

import android.app.Activity
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.databinding.FragmentSignUpContinueBinding
import com.bchmsl.midterm_weather.model.User
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.ui.signup.signupfirst.checkEmpty
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignUpContinueFragment : BaseFragment<FragmentSignUpContinueBinding>(FragmentSignUpContinueBinding::inflate) {
    // firebaseAuth
    private lateinit var firebaseAuth : FirebaseAuth
    private  lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null
    private var firstName = ""
    private var lastName = ""
    private var uid: String? = null
    override fun start() {
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()
        //get current user main info
        val firebaseUser  = firebaseAuth.currentUser
        //user id
        uid = firebaseUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        binding.apply {
            ibtnChoosePhoto.setOnClickListener {
                ImagePicker.Companion.with(this@SignUpContinueFragment)
                    .crop(150f,150f)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }

            }
            ibtnNext.setOnClickListener {
                when {
                    checkEmpty(tilFirstName) || checkEmpty(tilLastName)  -> {}
                    imageUri==null -> {
                        Snackbar.make(binding.root, "Please upload an image", Snackbar.LENGTH_SHORT)
                            .setTextMaxLines(1)
                            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.regular_red))
                            .show()
                    }
                    else -> {
                        pbSignup.visibility = View.VISIBLE
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
                                    Snackbar.make(binding.root, "failed to add additional sign up data  ${it.message}", Snackbar.LENGTH_SHORT)
                                        .setTextMaxLines(1)
                                        .setBackgroundTint(ContextCompat.getColor(requireContext(),
                                            R.color.regular_red
                                        ))
                                        .show()
                                }

                        } else {
                            hideProgressBar()
                            Snackbar.make(binding.root, "error: user ID is null", Snackbar.LENGTH_SHORT)
                                .setTextMaxLines(1)
                                .setBackgroundTint(ContextCompat.getColor(requireContext(),
                                    R.color.regular_red
                                ))
                                .show()


                        }
                    }
                }
            }
        }
    }

    private fun uploadProfilePic() {
        storageReference = FirebaseStorage.getInstance().getReference("Use rs/$uid")
        storageReference.putFile(imageUri!!).addOnSuccessListener {
            hideProgressBar()
            Snackbar.make(binding.root, "Registration was successful", Snackbar.LENGTH_SHORT)
                .setTextMaxLines(1)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.lime))
                .show()
            goToMainFra()
        }.addOnFailureListener {
            hideProgressBar()
            Snackbar.make(binding.root, "Failed to upload this image: ${it.message}", Snackbar.LENGTH_SHORT)
                .setTextMaxLines(1)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.regular_red))
                .show()
        }
    }

    private fun goToMainFra(){
        findNavController().navigate(SignUpContinueFragmentDirections.actionSignUpContinueFragmentToMainFragment())
    }

    private fun hideProgressBar() {
        binding.pbSignup.visibility = View.GONE
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
                    Snackbar.make(binding.root, ImagePicker.getError(data), Snackbar.LENGTH_SHORT)
                        .setTextMaxLines(1)
                        .setBackgroundTint(ContextCompat.getColor(requireContext(),
                            R.color.regular_red
                        ))
                        .show()
                }
                else -> {
                    Snackbar.make(binding.root, "Task Cancelled", Snackbar.LENGTH_SHORT)
                        .setTextMaxLines(1)
                        .setBackgroundTint(ContextCompat.getColor(requireContext(),
                            R.color.regular_red
                        ))
                        .show()
                }
            }
        }

}