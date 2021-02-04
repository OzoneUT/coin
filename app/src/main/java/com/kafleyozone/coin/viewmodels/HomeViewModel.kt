package com.kafleyozone.coin.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
    }

    private var _authorized = MutableLiveData<Boolean>()
    val authorized: LiveData<Boolean>
        get() = _authorized

    private var _userId = MutableLiveData<String>()
    val userData: LiveData<String>
        get() = _userId

    fun setUserId(id: String) {
        if (id.isEmpty()) {
            Log.e(TAG, "FATAL: userId was empty!")
            _authorized.value = false
            return
        }
        _authorized.value = true
        _userId.postValue(id)
    }
}