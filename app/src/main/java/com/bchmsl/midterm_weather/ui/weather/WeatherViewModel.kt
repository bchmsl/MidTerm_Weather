package com.bchmsl.midterm_weather.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bchmsl.midterm_weather.model.ForecastResponse
import com.bchmsl.midterm_weather.model.SearchResponse
import com.bchmsl.midterm_weather.network.RetrofitProvider
import com.bchmsl.midterm_weather.network.utils.ResponseHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    companion object {
        private val apiClient by lazy { RetrofitProvider.getClient() }
    }

    private val _searchResponse = MutableSharedFlow<ResponseHandler<SearchResponse>>()
    val searchResponse get() = _searchResponse.asSharedFlow()

    private var _forecastResponse: Flow<ResponseHandler<ForecastResponse>>? = null
    val forecastResponse get() = _forecastResponse

    fun getForecast(city: String = "Tbilisi") {
        viewModelScope.launch {
            _forecastResponse = flow{
                try {
                    val response = apiClient.getForecast(query = city)
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            emit(ResponseHandler.Success(body))
                        } else {
                            emit(ResponseHandler.Error(Throwable("Unresolved response")))
                        }
                    } else {
                        emit(ResponseHandler.Error(Throwable("Connection timed out.")))
                    }
                } catch (e: Throwable) {
                    emit(ResponseHandler.Error(e))
                }
            }
        }
    }
}