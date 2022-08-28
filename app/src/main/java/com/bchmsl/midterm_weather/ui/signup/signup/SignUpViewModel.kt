package com.bchmsl.midterm_weather.ui.signup.signup

import androidx.lifecycle.ViewModel
import com.bchmsl.midterm_weather.utils.ResponseHandler
import com.bchmsl.midterm_weather.utils.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel: ViewModel() {

    private val _signUpResponse = MutableStateFlow<ResponseHandler<FirebaseUser>>(ResponseHandler.Loading())
    val signUnResponse get() = _signUpResponse.asStateFlow()

    fun signupUser(email: String, password:String){
        Firebase.signUp(email, password).addOnSuccessListener {
            _signUpResponse.tryEmit(ResponseHandler.Success(it.user))
        }.addOnFailureListener {
            _signUpResponse.tryEmit(ResponseHandler.Error(it))
        }
    }
}