package com.kafleyozone.coin.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafleyozone.coin.data.AppRepository
import com.kafleyozone.coin.data.domain.BankInstitutionEntity
import com.kafleyozone.coin.data.domain.User
import com.kafleyozone.coin.data.network.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSetupFragmentViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    companion object {
        private const val TAG = "AccountSetupFragmentViewModel"
    }

    private var _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private var _setupBankList = MutableLiveData<MutableList<BankInstitutionEntity>>()
    val setupBankList: LiveData<MutableList<BankInstitutionEntity>>
        get() = _setupBankList

    private var _setupRes = MutableLiveData<Resource<User>>()
    val setupRes: LiveData<Resource<User>>
        get() = _setupRes

    init {
        _setupBankList.value = mutableListOf()
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun addBankAccount(name: String, description: String, amount: Double) {
        val mutableList = _setupBankList.value
        mutableList?.add(0, BankInstitutionEntity(name, description, amount))
        _setupBankList.value = mutableList!!
    }

    fun removeBankEntityItemAt(position: Int) {
        val mutableList = _setupBankList.value
        mutableList?.removeAt(position)
        _setupBankList.value = mutableList!!
    }

    fun doAccountSetup() {
        setupBankList.value?.let {
            viewModelScope.launch {
                try {
                    _setupRes.postValue(Resource.loading(null))
                    appRepository.setupAccount(it.toList()).let {
                        _setupRes.postValue(it)
                    }
                } catch (e: Exception) {
                    _setupRes.postValue(
                        Resource.error(
                            "We couldn't connect to the Coin server.", null
                        )
                    )
                    Log.e(TAG, "account setup failed: ${e.message}")
                }
            }
        }
    }
}