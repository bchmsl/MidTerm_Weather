package com.bchmsl.midterm_weather.extensions

import android.content.Context
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.app.App
import com.google.android.material.snackbar.Snackbar

fun View.makeSnackbar(s: String){
    Snackbar.make(this, s, Snackbar.LENGTH_SHORT).show()
}
fun View.makeErrorSnackbar(s: String){
    Snackbar.make(this, s, Snackbar.LENGTH_SHORT)
        .setTextMaxLines(2)
        .setBackgroundTint(ContextCompat.getColor(App.context, R.color.regular_red))
        .show()
}
fun View.makeSuccessSnackbar(s: String){
    Snackbar.make(this, s, Snackbar.LENGTH_SHORT)
        .setTextMaxLines(2)
        .setBackgroundTint(ContextCompat.getColor(App.context, R.color.lime))
        .show()
}