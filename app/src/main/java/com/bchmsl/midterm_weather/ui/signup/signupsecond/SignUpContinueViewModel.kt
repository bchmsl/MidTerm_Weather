package com.bchmsl.midterm_weather.ui.signup.signupsecond

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.bchmsl.midterm_weather.utils.firebase.Firebase
import com.bchmsl.midterm_weather.model.User
import com.bchmsl.midterm_weather.utils.ResponseHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpContinueViewModel : ViewModel() {

    private val _uploadProfilePicResponse =
        MutableStateFlow<ResponseHandler<Unit>>(ResponseHandler.Loading())
    val uploadProfilePicResponse get() = _uploadProfilePicResponse.asStateFlow()

    fun uploadProfilePic(uid: String?, imageUri: Uri?) {
        Firebase.uploadProfilePic(uid, imageUri).addOnSuccessListener {
            _uploadProfilePicResponse.tryEmit(ResponseHandler.Success(Unit))
        }.addOnFailureListener {
            _uploadProfilePicResponse.tryEmit(ResponseHandler.Error(it))
        }
    }

    private val _addToDatabaseResponse =
        MutableStateFlow<ResponseHandler<Unit>>(ResponseHandler.Loading())
    val addToDatabaseResponse get() = _addToDatabaseResponse.asStateFlow()

    fun addToDatabase(uid: String, user: User) {
        Firebase.addToDatabase(uid, user).addOnCompleteListener {
            _addToDatabaseResponse.tryEmit(ResponseHandler.Success(Unit))
        }.addOnFailureListener {
            _addToDatabaseResponse.tryEmit(ResponseHandler.Error(it))
        }
    }
}