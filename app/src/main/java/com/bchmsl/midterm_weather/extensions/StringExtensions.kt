package com.bchmsl.midterm_weather.extensions

import java.text.SimpleDateFormat
import java.util.*

fun String.toWeekday(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    val date = inputFormat.parse(this) as Date
    return outputFormat.format(date)
}

