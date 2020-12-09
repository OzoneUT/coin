package com.kafleyozone.coin

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.kafleyozone.coin.databinding.FragmentRegistrationBinding
import java.util.*
import kotlin.concurrent.schedule

class RegistrationFragmentViewModel : ViewModel() {

    private val _registrationSuccessState = MutableLiveData<Boolean>()
    val registrationSuccessState: LiveData<Boolean>
        get() = _registrationSuccessState

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
    fun mockNetworkCallForRegistration() {
        Timer().schedule(3000) {
            _registrationSuccessState.postValue(true)
        }
    }
}