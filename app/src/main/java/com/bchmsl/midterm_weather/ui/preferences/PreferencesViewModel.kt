package com.bchmsl.midterm_weather.ui.preferences

import androidx.lifecycle.ViewModel
import com.bchmsl.midterm_weather.firebase.Firebase

class PreferencesViewModel : ViewModel() {
    fun signOut() = Firebase.signOut()
}