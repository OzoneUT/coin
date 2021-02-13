package com.kafleyozone.coin.utils

import android.content.Context
import android.icu.text.DecimalFormat
import android.util.Log
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import java.util.*


/*
* Utility function to set isEnabled to multiple views at once
* */
fun List<Int>.setEnabledById(isEnabled: Boolean, view: View?) {
    for (id in this) view?.findViewById<View>(id)?.isEnabled = isEnabled
}

fun convertStringToFormattedCurrency(value: Double, symbol: Boolean = false): String {
    val fractionDigits = 2
    val format = DecimalFormat.getInstance(Locale.US) as DecimalFormat
    format.apply {
        maximumFractionDigits = fractionDigits
        minimumFractionDigits = fractionDigits
    }
    val formatted = format.format(value)
    return if (symbol) '$'.plus(formatted) else formatted
}

/*
* Gets the current date's formatter string with the correct day-of-month suffix (b/c English is
* hella annoying and inconsistent).
* */
fun getDateFormatPattern(): String {
    val now = Calendar.getInstance()
    val daySuffix = when (now.get(Calendar.DAY_OF_MONTH)) {
        1 -> "st"
        21 -> "st"
        31 -> "st"
        2 -> "nd"
        22 -> "nd"
        3 -> "rd"
        23 -> "rd"
        else -> "th"
    }
    return "EEEE, MMM. d'$daySuffix', yyyy"
}

fun printListDebug(tag: String, list: List<Any?>?) {
    if (list == null) {
        Log.i(tag, "list was null!")
        return
    } else {
        if (list.isEmpty()) {
            Log.i(tag, "[]")
            return
        }
        list.forEachIndexed { index: Int, obj: Any? ->
            Log.i(tag, "item $index: ${obj.toString()}")
        }
    }
}

/*
* Sets margin on view. Pass values in dp, they will get converted to px.
* */
fun View.setMargins(
        context: Context, left: Int = 0, top: Int = 0, right: Int = 0,
        bottom: Int = 0,
) {
    val l = context.dpToPx(left)
    val t = context.dpToPx(top)
    val r = context.dpToPx(right)
    val b = context.dpToPx(bottom)
    if (this.layoutParams is MarginLayoutParams) {
        val p = this.layoutParams as MarginLayoutParams
        p.setMargins(l, t, r, b)
        this.requestLayout()
    }
}

/*
* Converts given units of dp into px.
* */
fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}