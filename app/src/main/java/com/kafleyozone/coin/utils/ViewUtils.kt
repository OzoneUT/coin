package com.kafleyozone.coin.utils

import android.icu.text.DecimalFormat
import android.util.Log
import android.view.View
import java.util.*

    /*
    * Utility function to set isEnabled to multiple views at once
    * */
fun List<Int>.setEnabledById(isEnabled: Boolean, view: View?) {
    for (id in this) view?.findViewById<View>(id)?.isEnabled = isEnabled
}

fun convertStringToFormattedCurrency(value: Double, symbol: Boolean = false) : String {
    val fractionDigits = 2
    val format = DecimalFormat.getInstance(Locale.US) as DecimalFormat
    format.apply {
        maximumFractionDigits = fractionDigits
        minimumFractionDigits = fractionDigits
    }
    val formatted = format.format(value)
    return if (symbol) '$'.plus(formatted) else formatted
}

fun printListDebug(tag: String, list: List<Any?>?) {
    if (list == null) {
        Log.i(tag, "list was null!")
        return
    }
    else {
        if (list.isEmpty()) {
            Log.i(tag, "[]")
            return
        }
        list.forEachIndexed { index: Int, obj: Any? ->
            Log.i(tag, "item $index: ${obj.toString()}")
        }
    }
}