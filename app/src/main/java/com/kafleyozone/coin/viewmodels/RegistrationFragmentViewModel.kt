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
import com.kafleyozone.coin.data.models.RegistrationRequest
import com.kafleyozone.coin.data.models.Resource
import com.kafleyozone.coin.databinding.FragmentRegistrationBinding
import com.kafleyozone.coin.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationFragmentViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registrationRes = MutableLiveData<Resource<String>>()
    val registrationRes: LiveData<Resource<String>>
        get() = _registrationRes

    private val _stateEmailTaken = MutableLiveData<Boolean>()
    val stateEmailTaken: LiveData<Boolean>
        get() = _stateEmailTaken

    // MEMBER FIELDS
    private val _registrationInputValidations = mutableMapOf(
            R.id.register_name_field to false,
            R.id.register_email_field to false,
            R.id.register_password_field to false,
            R.id.register_confirm_password_field to false)

    val registrationInputValidations: Map<Int, Boolean>
        get() = _registrationInputValidations

    private val allRegistrationFieldsValid: Boolean
        get() = registrationInputValidations.values.all { it }


    // UI HELPER METHODS
    fun validateRegistrationFields(view: View, binding: FragmentRegistrationBinding) : Boolean {
        for (viewId in registrationInputValidations.keys) {
            validateRegistrationFieldByInput(view.findViewById(viewId), binding = binding)
        }
        return allRegistrationFieldsValid
    }

    fun validateRegistrationFieldByInput(input: TextInputEditText, hasFocus: Boolean = false,
                                         binding: FragmentRegistrationBinding) {
        when (input.id) {
            R.id.register_name_field -> if (!hasFocus) {
                _registrationInputValidations[R.id.register_name_field] =
                        input.isNameValid(binding.registerNameFieldLayout)
            }
            R.id.register_email_field -> if (!hasFocus) {
                _registrationInputValidations[R.id.register_email_field] =
                        input.isEmailValid(binding.registerEmailFieldLayout)
            }
            R.id.register_password_field -> if (!hasFocus) {
                _registrationInputValidations[R.id.register_password_field] =
                        input.isPasswordValid(binding.registerPasswordFieldLayout)
                _registrationInputValidations[R.id.register_confirm_password_field] =
                        binding.registerConfirmPasswordField
                                .isConfirmPasswordValid(binding.registerConfirmPasswordFieldLayout,
                                        input.text.toString())
            }
            R.id.register_confirm_password_field -> if (!hasFocus) {
                _registrationInputValidations[R.id.register_confirm_password_field] =
                        input.isConfirmPasswordValid(binding.registerConfirmPasswordFieldLayout,
                                binding.registerPasswordField.text.toString())
            }
        }
    }

    // REPOSITORY LOGIC
    fun doRegistration(request: RegistrationRequest) {
        viewModelScope.launch {
            try {
                _registrationRes.postValue(Resource.loading(null))
                userRepository.register(request).let {
                    if (it.status == Status.ERROR && it.data == UserRepository.EMAIL_TAKEN) {
                        _stateEmailTaken.postValue(true)
                        _registrationInputValidations[R.id.register_email_field] = false
                    }
                    _registrationRes.postValue(it)
                }
            } catch (e: Exception) {
                _registrationRes.postValue(Resource.error(
                        "We couldn't connect to the Coin server.", null))
                Log.e(LoginFragmentViewModel.TAG, "Registration failed: $e")
            }
        }
    }
}