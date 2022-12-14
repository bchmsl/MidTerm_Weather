package com.bchmsl.midterm_weather.network

import com.bchmsl.midterm_weather.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitProvider {
    private val retrofitClient by lazy{
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(WeatherApiClient::class.java)
    }

    fun getClient(): WeatherApiClient = retrofitClient
}