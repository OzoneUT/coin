package com.kafleyozone.coin.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafleyozone.coin.data.AppRepository
import com.kafleyozone.coin.data.AuthRepository
import com.kafleyozone.coin.data.domain.User
import com.kafleyozone.coin.data.network.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashFragmentViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appRepository: AppRepository
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
                if (authRepository.checkAuth()) {
                    _userRes.postValue(Resource.loading(null))
                    appRepository.getAuthorizedUserFromDB(authRepository.getLocalUser())?.let {
                        _userRes.postValue(Resource.success(it))
                        return@launch
                    }
                    appRepository.getAccount().let {
                        _userRes.postValue(it)
                    }
                } else {
                    throw IllegalStateException("no cached auth")
                }
            } catch (e: Exception) {
                _userRes.postValue(
                    Resource.error(
                        "couldn't get user data automatically",
                        null
                    )
                )
                Log.i(TAG, "couldn't get user data automatically")
            }
        }
    }

    fun getUserEmail() {
        viewModelScope.launch {
            val cachedEmail = authRepository.getLocalUser()
            _cachedEmail.postValue(cachedEmail)
            Log.i(TAG, "cached email: $cachedEmail")
        }
    }
}