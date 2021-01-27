package com.kafleyozone.coin.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafleyozone.coin.data.UserRepository
import kotlinx.coroutines.launch

class SplashFragmentViewModel @ViewModelInject constructor(
        private val userRepository: UserRepository
): ViewModel() {

    private var _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email


    fun getUser() {
        viewModelScope.launch {
            _email.postValue(userRepository.getLocalUser())
        }
    }
}