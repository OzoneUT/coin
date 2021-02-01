package com.kafleyozone.coin.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import com.kafleyozone.coin.R
import com.kafleyozone.coin.data.UserRepository
import com.kafleyozone.coin.data.models.LoginResponse
import com.kafleyozone.coin.data.models.Resource
import com.kafleyozone.coin.databinding.FragmentLoginBinding
import com.kafleyozone.coin.utils.isEmailValid
import com.kafleyozone.coin.utils.isPasswordValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.Credentials
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    companion object {
        const val TAG = "LoginFragmentViewModel"
    }

    // MEMBER FIELDS
    private val _loginRes = MutableLiveData<Resource<LoginResponse>>()
    val loginRes: LiveData<Resource<LoginResponse>>
        get() = _loginRes

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
                        input.isEmailValid(binding.loginEmailFieldLayout, false)
            }
            R.id.login_password_field -> if (!hasFocus) {
                _loginInputValidations[R.id.login_password_field] =
                        input.isPasswordValid(binding.loginPasswordFieldLayout, false)
            }
        }
    }

    // REPOSITORY LOGIC
    fun doLogin(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loginRes.postValue(Resource.loading(null))
                val basicValue = Credentials.basic(email, password)
                userRepository.login(basicValue).let {
                    _loginRes.postValue(it)
                }
            } catch (e: Exception) {
                _loginRes.postValue(Resource.error("We couldn't connect to the Coin server.",
                    null))
                Log.e(TAG, "Login failed, stacktrace:")
                e.printStackTrace()
            }
        }
    }

}