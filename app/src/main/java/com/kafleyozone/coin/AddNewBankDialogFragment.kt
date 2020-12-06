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

    private val viewModel: AccountSetupFragmentViewModel by viewModels(
            ownerProducer = {requireParentFragment()})

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
//        binding.setupAmountField.setOnFocusChangeListener { view: View, hasFocus: Boolean ->
//            if (!hasFocus) {
//                (view as TextInputEditText).isMonetaryAmountValid(binding.setupAmountFieldLayout)
//            }
//        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentAddNewBankDialogBinding.inflate(LayoutInflater.from(context))
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Add an account")
            builder.setPositiveButton("Confirm") { dialogInterface: DialogInterface, i: Int ->
                val name = chipResIdToName(binding.addBankDialogChipGroup.checkedChipId)
            }
            builder.setNegativeButton("Cancel", null)
            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun chipResIdToName(checkedChipId: Int): String =
        when(checkedChipId) {
            binding.checkingChip.id -> BankInstitutionEntity.Type.Checking.name
            binding.savingsChip.id -> BankInstitutionEntity.Type.Savings.name
            binding.cashChip.id -> BankInstitutionEntity.Type.Cash.name
            else -> "unknown_type"
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}