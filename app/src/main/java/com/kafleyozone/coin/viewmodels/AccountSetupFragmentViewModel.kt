package com.kafleyozone.coin.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafleyozone.coin.data.AppRepository
import com.kafleyozone.coin.data.models.BankInstitutionEntity
import com.kafleyozone.coin.data.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSetupFragmentViewModel @Inject constructor(
        private val appRepository: AppRepository,
) : ViewModel() {

    private var _setupBankList = MutableLiveData<MutableList<BankInstitutionEntity>>()
    val setupBankList: LiveData<MutableList<BankInstitutionEntity>>
        get() = _setupBankList

    private var _setupRes = MutableLiveData<Resource<String>>()
    val setupRes: LiveData<Resource<String>>
        get() = _setupRes

    init {
        _setupBankList.value = mutableListOf()
    }

    fun addBankAccount(name: String, description: String, amount: Double) {
        val mutableList = _setupBankList.value
        mutableList?.add(0, BankInstitutionEntity(name, description, amount))
        _setupBankList.value = mutableList
    }

    fun removeBankEntityItemAt(position: Int) {
        val mutableList = _setupBankList.value
        mutableList?.removeAt(position)
        _setupBankList.value = mutableList
    }

    fun doAccountSetup() {
        viewModelScope.launch {
            try {
                _setupRes.postValue(Resource.loading(null))
            } catch (e: Exception) {
                _setupRes.postValue(Resource.error(
                        "We couldn't connect to the Coin server.", null))
                Log.e(LoginFragmentViewModel.TAG, "Registration failed: $e")
            }
        }
    }
}