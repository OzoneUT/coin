package com.kafleyozone.coin

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.kafleyozone.coin.databinding.FragmentAddNewBankDialogBinding

class AddNewBankDialogFragmentViewModel : ViewModel() {

    companion object {
        const val TAG = "AddNewBankDialogFragmentViewModel"
        const val KEY_BANK_NAME = "name"
        const val KEY_BANK_TYPE = "type"
        const val KEY_AMOUNT = "amount"
        const val VAL_CASH_TYPE = "Cash/Wallet"
        const val VAL_UNKNOWN = "unknown_type"
    }

    private val _newBankDialogInputValidations = mutableMapOf(
            R.id.institution_name_field to false,
            R.id.setup_amount_field to false
    )

    val newBankDialogInputValidations get() = _newBankDialogInputValidations
    private val allNewBankDialogFieldsValid: Boolean
        get() = newBankDialogInputValidations.values.all { it }

    fun validateNewBankDialogFieldByInput(input: TextInputEditText, hasFocus: Boolean = false,
                                          binding: FragmentAddNewBankDialogBinding) {
        when (input.id) {
            R.id.institution_name_field -> if (!hasFocus) {
                _newBankDialogInputValidations[R.id.institution_name_field] = input.isNameValid()
            }
            R.id.setup_amount_field -> if (!hasFocus) {
                newBankDialogInputValidations[R.id.setup_amount_field] =
                        input.isMonetaryAmountValid(binding.setupAmountFieldLayout)
            }
            else -> {
                Log.e(TAG, "An unknown viewID was passed into validateNewBankDialogFieldByInput")
                return
            }
        }
    }

    fun validateNewBankDialogFields(view: View, binding: FragmentAddNewBankDialogBinding) : Boolean {
        for (viewId in newBankDialogInputValidations.keys) {
            validateNewBankDialogFieldByInput(view.findViewById(viewId), binding = binding)
        }
        return allNewBankDialogFieldsValid
    }

    fun chipResIdToBankType(checkedChipId: Int): String {
        return when (checkedChipId) {
            R.id.checking_chip -> {
                BankInstitutionEntity.Type.Checking.name
            }
            R.id.savings_chip -> {
                BankInstitutionEntity.Type.Savings.name
            }
            R.id.cash_chip -> {
                VAL_CASH_TYPE
            }
            else -> VAL_UNKNOWN
        }
    }


}