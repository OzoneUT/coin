package com.kafleyozone.coin.utils

import android.content.Context
import android.icu.text.DecimalFormat
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import com.google.android.material.transition.MaterialFadeThrough
import java.text.SimpleDateFormat
import java.util.*


/*
* Utility function to set isEnabled to multiple views at once
* */
fun List<Int>.setEnabledById(isEnabled: Boolean, view: View?) {
    for (id in this) view?.findViewById<View>(id)?.isEnabled = isEnabled
}

fun convertDoubleToFormattedCurrency(value: Double, symbol: Boolean = false): String {
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
* Sets margin on view. Pass values in dp, they will get converted to px.
* */
fun View.setMargins(
        context: Context, left: Int = 0, top: Int = 0, right: Int = 0,
        bottom: Int = 0
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

/*
* Creates and returns a MaterialFadeThrough transition without the secondary scale effect
* */
fun fadeThroughTransition(): MaterialFadeThrough {
    return MaterialFadeThrough().apply {
        secondaryAnimatorProvider = null
    }
}

/*
* Transforms a date in milliseconds to a formatted date String
* */
fun formatDate(millis: Long): String {
    return (SimpleDateFormat.getDateInstance() as SimpleDateFormat).let {
        it.applyPattern("EEEE, MMM. d, yyyy")
        it.timeZone = TimeZone.getTimeZone("UTC")
        it.format(millis)
    }
}