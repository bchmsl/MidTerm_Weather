package com.bchmsl.midterm_weather.extensions

import com.google.android.material.textfield.TextInputLayout


fun TextInputLayout.isFieldEmpty(): Boolean {
    return if (this.editText?.text.toString().trim().isEmpty()) {
        this.helperText = "Required"
        true
    } else {
        this.helperText = ""
        false
    }
}

fun TextInputLayout.isValidEmail(): Boolean {
    val email = this.editText?.text.toString().trim()
    return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        this.helperText = "invalid email"
        false
    } else {
        this.helperText = ""
        true
    }
}

fun TextInputLayout.notGoodPass(): Boolean {
    val password = this.editText?.text.toString()
    when {
        password.length <= 8 -> {
            this.helperText = "Password should be more than 8 characters"
            return true
        }
        password.contains(Regex("^[0-9]+[^a-zA-z]*$")) -> {
            this.helperText = "Password should contain at least one alphabet character"
            return true
        }
        password.contains(Regex("^[a-zA-Z]+[^0-9]*$")) -> {
            this.helperText = "Password should contain at least one numeric character"
            return true
        }
        else -> {
            this.helperText = ""
            return false
        }
    }
}