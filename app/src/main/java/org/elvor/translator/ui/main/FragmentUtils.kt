package org.elvor.translator.ui.main

import android.content.Context
import androidx.appcompat.app.AlertDialog
import org.elvor.translator.R

fun showErrorNotification(context: Context, message: String?) {
    AlertDialog.Builder(context)
        .setTitle(R.string.error_title)
        .setMessage(message ?: context.getString(R.string.error_message_unknown))
        .setNeutralButton(android.R.string.ok) { dialog, _ -> dialog.cancel() }
        .create()
        .show()
}

