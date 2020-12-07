package com.kafleyozone.coin

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.kafleyozone.coin.databinding.FragmentLoginBinding
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
    fun validateLoginFields(view: View, binding: FragmentLoginBinding) : Boolean {
        for (viewId in loginInputValidations.keys) {
            validateLoginFieldByInput(view.findViewById(viewId), binding = binding)
        }
        return allLoginFieldsValid
    }

    private fun validateLoginFieldByInput(input: TextInputEditText, hasFocus: Boolean = false,
                                          binding: FragmentLoginBinding) {
        when(input.id) {
            R.id.login_email_field -> if (!hasFocus) {
                _loginInputValidations[R.id.login_email_field] =
                        input.isEmailValid(binding.loginEmailFieldLayout, showDetailedHint = false)
            }
            R.id.login_password_field -> if (!hasFocus) {
                _loginInputValidations[R.id.login_password_field] =
                        input.isPasswordValid(binding.loginPasswordFieldLayout, showDetailedHint = false)
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