package com.kafleyozone.coin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccountSetupFragmentViewModel : ViewModel() {

    private var _setupBankList = MutableLiveData<MutableList<BankInstitutionEntity>>()
    val setupBankList: LiveData<MutableList<BankInstitutionEntity>>
        get() = _setupBankList

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
}