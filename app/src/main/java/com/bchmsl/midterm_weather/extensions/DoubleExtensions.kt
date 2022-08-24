package com.bchmsl.midterm_weather.extensions

fun Double.asTemp(): String {
    return this.toInt().plus("°")
}