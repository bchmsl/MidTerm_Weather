package com.bchmsl.midterm_weather.extensions

operator fun Any.plus(s: String): String {
    return this.toString() + s
}