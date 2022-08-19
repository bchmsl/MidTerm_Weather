package com.bchmsl.midterm_weather.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bchmsl.midterm_weather.model.ForecastResponse
import com.bchmsl.midterm_weather.model.SearchResponse
import com.bchmsl.midterm_weather.network.RetrofitProvider
import com.bchmsl.midterm_weather.network.utils.ResponseHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    companion object {
        private val apiClient by lazy { RetrofitProvider.getClient() }
    }

    private val _searchResponse = MutableSharedFlow<ResponseHandler<SearchResponse>>()
    val searchResponse get() = _searchResponse.asSharedFlow()

    private val _forecastResponse =
        MutableStateFlow<ResponseHandler<ForecastResponse>>(ResponseHandler.Loading())
    val forecastResponse get() = _forecastResponse.asStateFlow()

    fun getForecast(city: String = "Tbilisi") {
        viewModelScope.launch {
            try {
                val response = apiClient.getForecast(query = city)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _forecastResponse.emit(ResponseHandler.Success(body))
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
}