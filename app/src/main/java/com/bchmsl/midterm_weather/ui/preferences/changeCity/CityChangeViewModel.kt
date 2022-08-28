package com.bchmsl.midterm_weather.ui.preferences.changeCity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bchmsl.midterm_weather.app.App
import com.bchmsl.midterm_weather.models.SearchResponse
import com.bchmsl.midterm_weather.network.RetrofitProvider
import com.bchmsl.midterm_weather.utils.ResponseHandler
import com.bchmsl.midterm_weather.utils.datastore.DataStoreProvider.saveCity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CityChangeViewModel: ViewModel() {
    companion object {
        private val apiClient = RetrofitProvider.getClient()
    }

    private val _searchResponse = MutableSharedFlow<ResponseHandler<List<SearchResponse>>>()
    val searchResponse get() = _searchResponse.asSharedFlow()

    fun searchCity(query: String) {
        viewModelScope.launch {
            _searchResponse.emit(ResponseHandler.Loading())
            try {
                val response = apiClient.searchCities(query = query)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _searchResponse.emit(ResponseHandler.Success(body))
                    } else {
                        _searchResponse.emit(ResponseHandler.Error(Throwable("Unresolved response")))
                    }
                } else {
                    _searchResponse.emit(ResponseHandler.Error(Throwable("Connection timed out.")))
                }
            } catch (e: Throwable) {
                _searchResponse.emit(ResponseHandler.Error(e))
            }
        }
    }

    suspend fun saveCity(query: SearchResponse){
        if (query.name != null) {
            App.context.saveCity(query.name)
        }
    }
}