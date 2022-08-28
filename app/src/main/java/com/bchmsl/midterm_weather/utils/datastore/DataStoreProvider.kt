package com.bchmsl.midterm_weather.utils.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

object DataStoreProvider {
    private const val DATASTORE_NAME = "cityDataStore"
    private val CITY_PREFERENCE_KEY = stringPreferencesKey("city")
    private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

    suspend fun Context.writeData(cityName: String) {
        dataStore.edit {
            it[CITY_PREFERENCE_KEY] = cityName
        }
    }

    suspend fun Context.readDatastoreData(defaultValue: String): String {
        return dataStore.data.first()[CITY_PREFERENCE_KEY]?: defaultValue
    }

}