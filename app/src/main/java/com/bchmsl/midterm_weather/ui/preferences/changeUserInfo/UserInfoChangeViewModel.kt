package com.bchmsl.midterm_weather.ui.preferences.changeUserInfo

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.bchmsl.midterm_weather.models.User
import com.bchmsl.midterm_weather.utils.ResponseHandler
import com.bchmsl.midterm_weather.utils.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

class UserInfoChangeViewModel : ViewModel() {

    private val _userInfoResponse =
        MutableStateFlow<ResponseHandler<DataSnapshot>>(ResponseHandler.Loading())
    val userInfoResponse get() = _userInfoResponse.asStateFlow()

    fun getUserInfo() {
        Firebase.getUserInfo()?.addOnSuccessListener {
            if (it.exists()) {
                _userInfoResponse.tryEmit(ResponseHandler.Success(it))
            } else {
                _userInfoResponse.tryEmit(ResponseHandler.Error(Throwable("No logged in User's info was found")))
            }
        }?.addOnFailureListener {
            _userInfoResponse.tryEmit(ResponseHandler.Error(it))
        }
    }

    private val _userImageResponse =
        MutableStateFlow<ResponseHandler<Unit>>(ResponseHandler.Loading())
    val userImageResponse get() = _userImageResponse.asStateFlow()

    fun getUserImage(file: File) {
        Firebase.getUserImage(file).addOnSuccessListener {
            _userImageResponse.tryEmit(ResponseHandler.Success(Unit))
        }.addOnFailureListener {
            _userImageResponse.tryEmit(ResponseHandler.Error(it))
        }
    }

    fun updateUserInfo(firstName: String, lastName: String, imageUri: Uri?) {
        Firebase.updateUserInfo(firstName, lastName, imageUri)
    }

    fun isEmailVerified(): Boolean? {
        return Firebase.emailVerificationStatus()
    }

    fun sendEmailVerification() {
        Firebase.sendEmailVerification()
    }

    fun signOut() = Firebase.signOut()

    fun addEmptyPlaceHoldersToDatabase() {
        Firebase.addToDatabase(User("", ""))
    }
}