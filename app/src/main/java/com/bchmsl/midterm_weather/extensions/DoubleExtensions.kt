package com.bchmsl.midterm_weather.extensions

import kotlin.math.roundToInt

fun Double?.isPercent(of: Double?): String {
    if (this == null || of == null) {
        return "N/A"
    }
    return String.format("%.2f%%", this * 100 / of)
}

fun Double.toTemperature(isFahrenheit: Boolean): String {
    return if (isFahrenheit) {
        "${(this * 1.8 + 32).roundToInt()}°F"
    } else {
        "${this.roundToInt()}°C"
    }
}