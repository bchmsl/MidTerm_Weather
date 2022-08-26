package com.bchmsl.midterm_weather.ui.reset_password

import androidx.lifecycle.ViewModel
import com.bchmsl.midterm_weather.firebase.Firebase
import com.bchmsl.midterm_weather.network.utils.ResponseHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResetPasswordViewModel: ViewModel() {

    private val _resetPasswordResponse = MutableStateFlow<ResponseHandler<Unit>>(ResponseHandler.Loading())
    val resetPasswordResponse get() = _resetPasswordResponse.asStateFlow()

    fun resetPassword(email:String){
        Firebase.resetPassword(email).addOnSuccessListener {
            _resetPasswordResponse.tryEmit(ResponseHandler.Success(Unit))
        }.addOnFailureListener {
            _resetPasswordResponse.tryEmit(ResponseHandler.Error(it))
        }
    }
}