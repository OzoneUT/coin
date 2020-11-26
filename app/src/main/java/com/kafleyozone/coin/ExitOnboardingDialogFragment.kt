package com.kafleyozone.coin

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class ExitOnboardingDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "ExitOnboardingDialogFragment"
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.exit_onboarding_dialog_title))
            builder.setMessage(getString(R.string.exit_onboarding_dialog_message))
                .setPositiveButton(getString(R.string.exit_label)) { _: DialogInterface, _: Int ->
                    it.finish()
                }
                .setNegativeButton(getString(R.string.cancel_label)) { _: DialogInterface, _: Int ->
                    // user cancelled the dialog
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}