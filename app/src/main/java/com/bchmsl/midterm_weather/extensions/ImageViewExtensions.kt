package com.bchmsl.midterm_weather.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setImage(url: String?){
    Glide.with(this).load("https:$url").into(this)
}