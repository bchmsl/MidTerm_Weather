package com.bchmsl.midterm_weather.utils.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

object DataStoreProvider {
    private const val DATASTORE_NAME = "cityDataStore"
    private val CITY_PREFERENCE_KEY = stringPreferencesKey("city")
    private val UNIT_PREFERENCE_KEY = booleanPreferencesKey("unit")
    private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

    suspend fun Context.saveCity(cityName: String) {
        dataStore.edit {
            it[CITY_PREFERENCE_KEY] = cityName
        }
    }

    suspend fun Context.readCity(defaultValue: String): String {
        return dataStore.data.first()[CITY_PREFERENCE_KEY] ?: defaultValue
    }

    suspend fun Context.saveUnit(isFahrenheit: Boolean) {
        dataStore.edit {
            it[UNIT_PREFERENCE_KEY] = isFahrenheit
        }
    }

    suspend fun Context.readUnit(isDefaultFahrenheit: Boolean): Boolean {
        return dataStore.data.first()[UNIT_PREFERENCE_KEY] ?: isDefaultFahrenheit
    }

}