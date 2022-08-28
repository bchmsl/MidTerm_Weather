package com.bchmsl.midterm_weather.ui.resetPassword

import androidx.lifecycle.ViewModel
import com.bchmsl.midterm_weather.utils.ResponseHandler
import com.bchmsl.midterm_weather.utils.firebase.Firebase
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