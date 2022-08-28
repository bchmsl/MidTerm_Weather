package com.bchmsl.midterm_weather.network

import com.bchmsl.midterm_weather.BuildConfig
import com.bchmsl.midterm_weather.models.ForecastResponse
import com.bchmsl.midterm_weather.models.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiClient {
    @GET("search.json")
    suspend fun searchCities(
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Query("q") query: String
    ): Response<List<SearchResponse>>

    @GET("forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Query("q") query: String,
        @Query("days") daysCount: Int = 7,
        @Query("aqi") aqi: String = "yes"
    ):Response<ForecastResponse>
}