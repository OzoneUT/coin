package com.kafleyozone.coin.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafleyozone.coin.data.AppRepository
import com.kafleyozone.coin.data.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
    }

    private var _authorized = MutableLiveData<Boolean>()
    val authorized: LiveData<Boolean>
        get() = _authorized

    private var _userData = MutableLiveData<User>()
    val userData: LiveData<User>
        get() = _userData

    fun getUserFromDB(id: String) {
        if (id.isEmpty()) {
            Log.e(TAG, "FATAL: userId was empty!")
            _authorized.value = false
            return
        }
        viewModelScope.launch {
            val user = appRepository.getAuthorizedUserFromDB(id)
            if (user != null) {
                _userData.postValue(user)
            } else {
                Log.e(TAG, "FATAL: cached user was null!")
                _authorized.postValue(false)
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            appRepository.logout()
            _authorized.postValue(false)
        }
    }
}