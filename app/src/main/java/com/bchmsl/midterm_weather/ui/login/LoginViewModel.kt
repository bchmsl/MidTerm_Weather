package com.bchmsl.midterm_weather.ui.login

import androidx.lifecycle.ViewModel
import com.bchmsl.midterm_weather.utils.firebase.Firebase
import com.bchmsl.midterm_weather.utils.ResponseHandler
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    private val _signInResponse = MutableStateFlow<ResponseHandler<FirebaseUser>>(ResponseHandler.Loading())
    val signInResponse get() = _signInResponse.asStateFlow()


    fun loginUser(email: String, password: String) {
        Firebase.signIn(email, password).addOnSuccessListener {
            _signInResponse.tryEmit(ResponseHandler.Success(it.user))
        }.addOnFailureListener {
            _signInResponse.tryEmit(ResponseHandler.Error(it))
        }
    }
}