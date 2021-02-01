package com.kafleyozone.coin.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafleyozone.coin.data.UserRepository
import com.kafleyozone.coin.data.models.Resource
import com.kafleyozone.coin.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashFragmentViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    companion object {
        private const val TAG = "SplashFragmentViewModel"
    }

    private var _userRes = MutableLiveData<Resource<User>>()
    val userRes: LiveData<Resource<User>>
        get() = _userRes

    private var _cachedEmail = MutableLiveData<String>()
    val cachedEmail: LiveData<String>
        get() = _cachedEmail

    fun initialize() {
        viewModelScope.launch {
            try {
                _userRes.postValue(Resource.loading(null))
                userRepository.getAccount().let {
                    _userRes.postValue(it)
                }
            } catch (e: Exception) {
                _userRes.postValue(
                    Resource.error(
                        "We couldn't connect to the Coin server.",
                        null
                    )
                )
                Log.e(TAG, "couldn't get user data automatically")
                e.printStackTrace()
            }
        }
    }

    fun getUserEmail() {
        viewModelScope.launch {
            val cachedEmail = userRepository.getLocalUser()
            _cachedEmail.postValue(cachedEmail)
            Log.i(TAG, "found cached email: $cachedEmail")
        }
    }
}