package com.kafleyozone.coin

import android.util.Patterns
import androidx.core.text.trimmedLength
import com.google.android.material.textfield.TextInputEditText

private const val MAX_LENGTH = 255
private const val PASSWORD_MIN_LENGTH = 7

fun TextInputEditText.isNameValid(): Boolean {
    return if (!text.isNullOrEmpty() && text!!.trimmedLength() <= MAX_LENGTH) {
        error = null
        true
    } else {
        error = context.getString(R.string.input_validation_name)
        false
    }
}

fun TextInputEditText.isEmailValid(): Boolean {
    return if (!text.isNullOrEmpty() &&
            text!!.trimmedLength() <= MAX_LENGTH &&
            text!!.matches(Patterns.EMAIL_ADDRESS.toRegex())) {
        error = null
        true
    } else {
        error = context.getString(R.string.input_validation_email)
        false
    }
}

fun TextInputEditText.isPasswordValid(): Boolean {
    return if (!text.isNullOrEmpty() && text!!.length >= PASSWORD_MIN_LENGTH) {
        error = null
        true
    } else {
        error = context.getString(R.string.input_validation_password, PASSWORD_MIN_LENGTH)
        false
    }
}

fun TextInputEditText.isConfirmPasswordValid(toMatch: String): Boolean {
    return if (!text.isNullOrEmpty() && text!!.toString() == toMatch) {
        error = null
        true
    } else {
        error = context.getString(R.string.input_validation_confirm_password)
        false
    }
}