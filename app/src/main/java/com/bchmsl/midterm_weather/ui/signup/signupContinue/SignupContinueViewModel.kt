package com.bchmsl.midterm_weather.ui.signup.signupContinue

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.bchmsl.midterm_weather.models.User
import com.bchmsl.midterm_weather.utils.ResponseHandler
import com.bchmsl.midterm_weather.utils.firebase.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignupContinueViewModel : ViewModel() {

    private val _uploadProfilePicResponse =
        MutableStateFlow<ResponseHandler<Unit>>(ResponseHandler.Loading())
    val uploadProfilePicResponse get() = _uploadProfilePicResponse.asStateFlow()

    fun uploadProfilePic(imageUri: Uri?) {
        Firebase.uploadProfilePic(imageUri).addOnSuccessListener {
            _uploadProfilePicResponse.tryEmit(ResponseHandler.Success(Unit))
        }.addOnFailureListener {
            _uploadProfilePicResponse.tryEmit(ResponseHandler.Error(it))
        }
    }

    private val _addToDatabaseResponse =
        MutableStateFlow<ResponseHandler<Unit>>(ResponseHandler.Loading())
    val addToDatabaseResponse get() = _addToDatabaseResponse.asStateFlow()

    fun addToDatabase(user: User) {
        Firebase.addToDatabase(user)?.addOnCompleteListener {
            _addToDatabaseResponse.tryEmit(ResponseHandler.Success(Unit))
        }?.addOnFailureListener {
            _addToDatabaseResponse.tryEmit(ResponseHandler.Error(it))
        }
    }
}