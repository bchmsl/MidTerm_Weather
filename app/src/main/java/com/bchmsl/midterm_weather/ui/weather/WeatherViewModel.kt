package com.bchmsl.midterm_weather.ui.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bchmsl.midterm_weather.app.App
import com.bchmsl.midterm_weather.models.ForecastResponse
import com.bchmsl.midterm_weather.network.RetrofitProvider
import com.bchmsl.midterm_weather.utils.ResponseHandler
import com.bchmsl.midterm_weather.utils.datastore.DataStoreProvider.readCity
import com.bchmsl.midterm_weather.utils.datastore.DataStoreProvider.readUnit
import com.bchmsl.midterm_weather.utils.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    companion object {
        private val apiClient by lazy { RetrofitProvider.getClient() }
    }

    private val _forecastResponse =
        MutableStateFlow<ResponseHandler<ForecastResponse>>(ResponseHandler.Loading())
    val forecastResponse get() = _forecastResponse.asStateFlow()

    fun getForecast(city: String) {
        viewModelScope.launch {
            try {
                val response = apiClient.getForecast(query = city)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _forecastResponse.emit(ResponseHandler.Success(body))
                        Log.wtf("TAG", "EMITTED")
                    } else {
                        _forecastResponse.emit(ResponseHandler.Error(Throwable("Unresolved response")))
                    }
                } else {
                    _forecastResponse.emit(ResponseHandler.Error(Throwable("Connection timed out.")))
                }
            } catch (e: Throwable) {
                _forecastResponse.emit(ResponseHandler.Error(e))
            }
        }
    }

    suspend fun getDatastoreValue() = flow {
        emit(App.context.readCity(defaultValue = "Tbilisi"))
    }


    private val _userFirstNameResponse =
        MutableStateFlow<ResponseHandler<DataSnapshot>>(ResponseHandler.Loading())
    val userFirstNameResponse get() = _userFirstNameResponse.asStateFlow()

    fun getUserFirstName() {
        Firebase.getUserFirstName()?.addOnSuccessListener {
            _userFirstNameResponse.tryEmit(ResponseHandler.Success(it))
        }?.addOnFailureListener {
            _userFirstNameResponse.tryEmit(ResponseHandler.Error(it))
        }
    }

    fun getUnit() = flow {
        emit(App.context.readUnit(false))
    }
}
