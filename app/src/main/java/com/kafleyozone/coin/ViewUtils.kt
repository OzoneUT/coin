package com.kafleyozone.coin

import android.view.View

/*
    * Utility function to set isEnabled to multiple views at once
    * */
fun List<Int>.setEnabledById(isEnabled: Boolean, view: View?) {
    for (id in this) view?.findViewById<View>(id)?.isEnabled = isEnabled
}