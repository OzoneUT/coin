package com.kafleyozone.coin

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class AddNewBankDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "AddNewAccountDialogFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Add an account")
            builder.setPositiveButton("Confirm", null)
            builder.setNegativeButton("Cancel", null)
            builder.setView(R.layout.fragment_add_new_bank_dialog)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}