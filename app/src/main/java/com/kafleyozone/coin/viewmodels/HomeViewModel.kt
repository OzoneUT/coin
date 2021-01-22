package com.kafleyozone.coin.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kafleyozone.coin.data.models.User

class HomeViewModel: ViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
    }

    private var _authorized = MutableLiveData<Boolean>()
    val authorized: LiveData<Boolean>
        get() = _authorized

    private var _userData = MutableLiveData<User>()
    val userData: LiveData<User>
        get() = _userData

    fun setUserData(u: User?) {
        if (u == null) {
            Log.e(TAG, "FATAL: user was null! Finishing activity.")
            _authorized.value = false
            return
        }
        _authorized.value = true
        _userData.postValue(u)
    }
}