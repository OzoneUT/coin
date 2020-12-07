package com.kafleyozone.coin

import android.icu.text.DecimalFormat
import android.util.Patterns
import androidx.core.text.trimmedLength
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.math.RoundingMode
import java.util.*
import kotlin.math.round

private const val MAX_STRING_LENGTH = 255
private const val MAX_MONETARY_AMOUNT = 999999.99
private const val MAX_MONETARY_AMOUNT_DISPLAY = "$999,999.99"
private const val PASSWORD_MIN_LENGTH = 7

fun TextInputEditText.isNameValid(layoutView: TextInputLayout): Boolean {
    return if (!text.isNullOrEmpty() && text!!.trimmedLength() <= MAX_STRING_LENGTH) {
        layoutView.error = null
        true
    } else {
        layoutView.error = context.getString(R.string.input_validation_name)
        false
    }
}

fun TextInputEditText.isEmailValid(layoutView: TextInputLayout, showDetailedHint: Boolean = true): Boolean {
    return if (!text.isNullOrEmpty() &&
            text!!.trimmedLength() <= MAX_STRING_LENGTH &&
            text!!.matches(Patterns.EMAIL_ADDRESS.toRegex())) {
        layoutView.error = null
        true
    } else {
        layoutView.error =
                if (showDetailedHint) context.getString(R.string.input_validation_email)
                else context.getString(R.string.input_validation_email_login)
        false
    }
}

fun TextInputEditText.isPasswordValid(layoutView: TextInputLayout, showDetailedHint: Boolean = true): Boolean {
    return if (!text.isNullOrEmpty() && text!!.length >= PASSWORD_MIN_LENGTH) {
        layoutView.error = null
        true
    } else {
        layoutView.error = if (showDetailedHint) context.getString(R.string.input_validation_password, PASSWORD_MIN_LENGTH)
                else context.getString(R.string.input_validation_password_login)
        false
    }
}

fun TextInputEditText.isConfirmPasswordValid(layoutView: TextInputLayout, toMatch: String): Boolean {
    return if (!text.isNullOrEmpty() && text!!.toString() == toMatch) {
        layoutView.error = null
        true
    } else {
        layoutView.error = context.getString(R.string.input_validation_confirm_password)
        false
    }
}

fun TextInputEditText.isMonetaryAmountValid(layoutView: TextInputLayout): Boolean {
    // Note: Should not have problems being parsed to a double because the field is number-only.
    if (text.isNullOrEmpty()){
        layoutView.error = "Enter a positive amount greater than zero."
        return false
    }

    val value = text.toString().replace(",", "").toDouble()
    if (value <= 0) {
        layoutView.error = "Enter a positive amount greater than zero."
        return false
    } else if (value >= MAX_MONETARY_AMOUNT) {
        layoutView.error = "Max amount: $MAX_MONETARY_AMOUNT_DISPLAY"
        return false
    }

    // valid entry. need to format.
    val fractionDigits = 2
    val format = DecimalFormat.getInstance(Locale.US) as DecimalFormat
    format.apply {
        maximumFractionDigits = fractionDigits
        minimumFractionDigits = fractionDigits
    }
    setText(format.format(value))

    layoutView.error = null
    return true
}