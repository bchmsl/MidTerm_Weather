package com.bchmsl.midterm_weather.ui.preferences.mainPreferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bchmsl.midterm_weather.app.App
import com.bchmsl.midterm_weather.utils.datastore.DataStoreProvider.readUnit
import com.bchmsl.midterm_weather.utils.datastore.DataStoreProvider.saveUnit
import com.bchmsl.midterm_weather.utils.firebase.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class PreferencesViewModel : ViewModel() {

    fun signOut() = Firebase.signOut()

    private var _unitFlow: Flow<Boolean>? = null
    val unitFlow get() = _unitFlow

    fun getCurrentUnit() = flow {
        emit(App.context.readUnit(isDefaultFahrenheit = false))

    }

    fun setUnit(unit: Boolean) {
        viewModelScope.launch {
            App.context.saveUnit(unit)
        }
    }
}