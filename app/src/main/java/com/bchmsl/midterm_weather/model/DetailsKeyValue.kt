package com.bchmsl.midterm_weather.model

import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.extensions.asTemp
import com.bchmsl.midterm_weather.extensions.plus
import com.bchmsl.midterm_weather.extensions.shorten

typealias D = R.drawable

data class DetailsKeyValue(
    val key: String?,
    val value: Any? = null,
    val listValue: List<DetailsKeyValue>? = null,
    val isDouble: Boolean = false,
    val image: Int? = null
) {
    object Data {
        private val mData = mutableListOf<DetailsKeyValue>()
        val data get() = mData.toList()

        private fun setDataToList(
            vararg detailsKeyValue: DetailsKeyValue
        ) {
            mData.clear()
            detailsKeyValue.forEach {
                mData.add(it)
            }
        }

        fun addDetailsData(chosenDay: ForecastResponse.Forecast.ForecastDay?) {
            setDataToList(
                DetailsKeyValue(
                    "Temperature",
                    listValue = listOf(
                        DetailsKeyValue(
                            "Max:",
                            chosenDay?.day?.maxtempC?.asTemp()
                        ),
                        DetailsKeyValue(
                            "Min",
                            chosenDay?.day?.mintempC?.asTemp()
                        )
                    ),
                    isDouble = true,
                    image = D.ic_temperature
                ),
                DetailsKeyValue(
                    "Wind Speed",
                    chosenDay?.day?.maxwindKph?.plus(" km/h"),
                    image = D.ic_wind
                ),
                DetailsKeyValue(
                    "Total Precipitation",
                    chosenDay?.day?.totalprecipMm?.plus(" mm"),
                    image = D.ic_precipitation
                ),
                DetailsKeyValue(
                    "Visibility",
                    chosenDay?.day?.avgvisKm?.plus(" km"),
                    image = D.ic_visibility
                ),
                DetailsKeyValue(
                    "Humidity",
                    chosenDay?.day?.avghumidity?.plus("%"),
                    image = D.ic_humidity
                ),
                DetailsKeyValue(
                    "Chance of rain",
                    chosenDay?.day?.dailyChanceOfRain?.plus("%"),
                    image = D.ic_chance_rain
                ),
                DetailsKeyValue(
                    "Sun Info",
                    listValue = listOf(
                        DetailsKeyValue(
                            "Sunrise",
                            chosenDay?.astro?.sunrise
                        ),
                        DetailsKeyValue(
                            "Sunset",
                            chosenDay?.astro?.sunset
                        ),
                    ),
                    isDouble = true,
                    image = D.ic_sunrise
                ),
                DetailsKeyValue(
                    "UV Index",
                    chosenDay?.day?.uv,
                    image = D.ic_uv_index
                ),
                DetailsKeyValue(
                    "Moon Info",
                    listValue = listOf(
                        DetailsKeyValue(
                            "Moonrise",
                            chosenDay?.astro?.moonrise
                        ),
                        DetailsKeyValue(
                            "Moonset",
                            chosenDay?.astro?.moonset
                        ),
                    ),
                    isDouble = true,
                    image = D.ic_moonrise
                ),
                DetailsKeyValue(
                    "Moon Illumination",
                    chosenDay?.astro?.moonIllumination?.plus("%"),
                    image = D.ic_moon_illumination
                ),
                DetailsKeyValue(
                    "Air Quality\nCO",
                    chosenDay?.day?.airQuality?.co?.toDouble()?.shorten(),
                    image = D.ic_air_quality
                ),
                DetailsKeyValue(
                    "Air Quality\nNO2",
                    chosenDay?.day?.airQuality?.no2?.toDouble()?.shorten(),
                    image = D.ic_air_quality

                ),
                DetailsKeyValue(
                    "Air Quality\nO3",
                    chosenDay?.day?.airQuality?.o3?.toDouble()?.shorten(),
                    image = D.ic_air_quality
                ),
                DetailsKeyValue(
                    "Air Quality\nSO2",
                    chosenDay?.day?.airQuality?.so2?.toDouble()?.shorten(),
                    image = D.ic_air_quality
                )

            )
        }
    }

}
