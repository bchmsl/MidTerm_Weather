package com.bchmsl.midterm_weather.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bchmsl.midterm_weather.firebase.Firebase
import com.bchmsl.midterm_weather.network.utils.ResponseHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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