package com.kafleyozone.coin

import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.util.Patterns
import androidx.core.text.trimmedLength
import com.google.android.material.textfield.TextInputEditText
import java.util.*

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

fun TextInputEditText.isEmailValid(showDetailedHint: Boolean = true): Boolean {
    return if (!text.isNullOrEmpty() &&
            text!!.trimmedLength() <= MAX_LENGTH &&
            text!!.matches(Patterns.EMAIL_ADDRESS.toRegex())) {
        error = null
        true
    } else {
        error =
                if (showDetailedHint) context.getString(R.string.input_validation_email)
                else context.getString(R.string.input_validation_email_login)
        false
    }
}

fun TextInputEditText.isPasswordValid(showDetailedHint: Boolean = true): Boolean {
    return if (!text.isNullOrEmpty() && text!!.length >= PASSWORD_MIN_LENGTH) {
        error = null
        true
    } else {
        error = if (showDetailedHint) context.getString(R.string.input_validation_password, PASSWORD_MIN_LENGTH)
                else context.getString(R.string.input_validation_password_login)
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

fun TextInputEditText.isMonetaryAmountValid(): Boolean {
    // Note: Should not have problems being parsed to a double because the field is number-only.
    if (text.isNullOrEmpty()){
        error = "Enter a non-zero amount."
        return false
    }
    val value = text.toString().replace(",", "").toDouble()
    if (value <= 0) {
        error = "Enter a non-zero amount."
        return false
    }

    val fractionDigits = 2
    val format: NumberFormat = NumberFormat.getInstance(Locale.US)
    format.apply {
        maximumFractionDigits = fractionDigits
        minimumFractionDigits = fractionDigits
    }
}