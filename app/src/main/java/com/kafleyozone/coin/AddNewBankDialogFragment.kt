package com.kafleyozone.coin

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.kafleyozone.coin.databinding.FragmentAddNewBankDialogBinding
import java.lang.IllegalStateException

class AddNewBankDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "AddNewAccountDialogFragment"
    }

    private var _binding: FragmentAddNewBankDialogBinding? = null
    private val binding get() = _binding!!
    private var dialogView: AlertDialog? = null

    private val sharedViewModel: AccountSetupFragmentViewModel by viewModels(
            ownerProducer = {requireParentFragment()})
    private val dialogViewModel: AddNewBankDialogFragmentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        setupValidationListeners()

        binding.addBankDialogChipGroup.setOnCheckedChangeListener { _, checkedId ->
            if (dialogViewModel.chipResIdToBankType(checkedId) == AddNewBankDialogFragmentViewModel.VAL_CASH_TYPE) {
                binding.institutionNameField.setText(BankInstitutionEntity.Type.Cash.name)
                binding.institutionNameFieldLayout.isEnabled = false
                binding.institutionNameFieldLayout.isErrorEnabled = false
            } else {
                binding.institutionNameField.setText("")
                binding.institutionNameFieldLayout.isEnabled = true
                binding.institutionNameFieldLayout.error = null
            }
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentAddNewBankDialogBinding.inflate(LayoutInflater.from(context))
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Add an account")
            builder.setPositiveButton("Confirm", null)
            builder.setNegativeButton("Cancel", null)
            builder.setView(binding.root)
            dialogView = builder.create()
            dialogView
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    /*
    * Helper function to setup listeners on newBankDialog input fields to fire when focus is lost
    * to validate the form on the fly
    * */
    private fun setupValidationListeners() {
        for (viewId in dialogViewModel.newBankDialogInputValidations.keys) {
            val input = binding.root.findViewById<TextInputEditText>(viewId)
            input.setOnFocusChangeListener { _: View, hasFocus: Boolean ->
                dialogViewModel.validateNewBankDialogFieldByInput(input, hasFocus, binding)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialogView?.getButton(DialogInterface.BUTTON_POSITIVE)?.setOnClickListener {
            if (dialogViewModel.validateNewBankDialogFields(requireView(), binding)) {
                val name = binding.institutionNameField.text.toString()
                val type = dialogViewModel.chipResIdToBankType(binding.addBankDialogChipGroup.checkedChipId)
                val amount = binding.setupAmountField.text.toString()
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}