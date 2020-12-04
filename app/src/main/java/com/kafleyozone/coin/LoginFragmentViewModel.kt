package com.kafleyozone.coin

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import kotlin.concurrent.schedule

class LoginFragmentViewModel : ViewModel() {

    // MEMBER FIELDS
    private val _loginSuccessState = MutableLiveData<Boolean>()
    val loginSuccessState: LiveData<Boolean>
        get() = _loginSuccessState

    private val _loginInputValidations: MutableMap<Int, Boolean> = mutableMapOf(
            R.id.login_email_field to false,
            R.id.login_password_field to false)

    val loginInputValidations: Map<Int, Boolean>
        get() = _loginInputValidations

    private val allLoginFieldsValid: Boolean
        get() = loginInputValidations.values.all { it }


    // UI VALIDATION LOGIC
    fun validateLoginFields(view: View) : Boolean {
        for (viewId in loginInputValidations.keys) {
            validateLoginFieldByInput(view.findViewById(viewId))
        }
        return allLoginFieldsValid
    }

    fun validateLoginFieldByInput(input: TextInputEditText, hasFocus: Boolean = false) {
        when(input.id) {
            R.id.login_email_field -> if (!hasFocus) {
                _loginInputValidations[R.id.login_email_field] = input.isEmailValid()
            }
            R.id.login_password_field -> if (!hasFocus) {
                _loginInputValidations[R.id.login_password_field] = input.isPasswordValid()
            }
        }
    }


    // REPOSITORY LOGIC
    fun mockNetworkCallForLogin() {
        Timer().schedule(2000) {
            _loginSuccessState.postValue(true)
        }
    }
}