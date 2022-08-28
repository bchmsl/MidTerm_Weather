package com.bchmsl.midterm_weather.extensions

fun Double.asTemp(): String {
    return this.toInt().plus("°")
}

fun Double?.isPercent(of: Double?): String {
    if (this == null || of == null) {
        return "N/A"
    }
    return String.format("%.2f%%", this * 100 / of)
}
