package com.kafleyozone.coin.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.kafleyozone.coin.data.AppRepository
import com.kafleyozone.coin.data.domain.SetupAmountEntity
import com.kafleyozone.coin.data.domain.User
import com.kafleyozone.coin.data.network.models.Resource
import com.kafleyozone.coin.utils.convertDoubleToFormattedCurrency
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

    private var _setupHistoryList = MutableLiveData<MutableList<SetupAmountEntity>>()
    val setupHistoryList: LiveData<MutableList<SetupAmountEntity>>
        get() = _setupHistoryList

    private var _setupHistorySum = MutableLiveData<Double>()
    val sumAmountFormatted = Transformations.map(_setupHistorySum) {
        convertDoubleToFormattedCurrency(it, true)
    }

    private var _setupRes = MutableLiveData<Resource<User>>()
    val setupRes: LiveData<Resource<User>>
        get() = _setupRes

    init {
        _setupHistoryList.value = mutableListOf()
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun addSetupAmountToHistory(amount: String) {
        amount.toDoubleOrNull()?.let {
            val mutableList = _setupHistoryList.value
            mutableList?.add(0, SetupAmountEntity(it))
            _setupHistoryList.value = mutableList!!
        }
    }

    fun removeSetupAmountFromHistory(item: SetupAmountEntity) {
        val mutableList = _setupHistoryList.value
        mutableList?.remove(item)
        _setupHistoryList.value = mutableList!!
    }

    fun calculateSetupHistorySum() {
        var sum = 0.0
        setupHistoryList.value?.forEach {
            sum += it.amount
        }
        _setupHistorySum.value = sum
    }

    fun doAccountSetup() {
        setupHistoryList.value?.let {
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