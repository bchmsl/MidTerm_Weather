package com.bchmsl.midterm_weather.extensions

fun Double.asTemp(): String {
    return this.toInt().plus("°")
}

fun Double.shorten(): String {
    return String.format("%.1f", this)
}