package com.kafleyozone.coin.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.kafleyozone.coin.R

class IndeterminateProgressDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val dialog = AlertDialog.Builder(it)
                .setView(R.layout.dialog_indeterminate_progress)
                .create()
            isCancelable = false
            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}