package com.bchmsl.midterm_weather.model

import com.squareup.moshi.Json
import java.io.Serializable


data class ForecastResponse(
    val location: Location?,
    val current: Current?,
    val forecast: Forecast?,
    ):Serializable {

    data class Location(
        val name: String?,
        val localtime: String?,
    )

    data class Current(

        @field:Json(name = "last_updated")
        val lastUpdated: String?,
        @field:Json(name = "temp_c")
        val tempC: Double?,
        @field:Json(name = "feelslike_c")
        val feelslikeC: Double?,
        val condition: Forecast.ForecastDay.Day.Condition,
        val humidity: Int?
    )

    data class Forecast(
        val forecastday: List<ForecastDay?>?
    ) {

        data class ForecastDay(
            val date: String?,
            @field:Json(name = "date_epoch")
            val dateEpoch: Long?,
            val day: Day?,
            val astro: Astro?,
        ): Serializable {

            data class Day(
                @field:Json(name = "maxtemp_c")
                val maxtempC: Double?,
                @field:Json(name = "mintemp_c")
                val mintempC: Double?,
                @field:Json(name = "avgtemp_c")
                val avgtempC: Double?,
                val condition: Condition?,
                val avghumidity:Double?,
                @field:Json(name = "totalprecip_mm")
                val totalprecipMm: Double,
                @field:Json(name = "maxwind_kph")
                val maxwindKph: Double,
                @field:Json(name = "daily_chance_of_rain")
                val dailyChanceOfRain: Int?,
                @field:Json(name = "avgvis_km")
                val avgvisKm: Double?,
                val uv: Double?
            ) {

                data class Condition(
                    val text: String?,
                    val icon: String?
                )
            }

            data class Astro(
                val sunrise: String?,
                val sunset: String?,
                val moonrise: String?,
                val moonset: String?,
                @field:Json(name = "moon_illumination")
                val moonIllumination: String
            )
        }
    }
}
