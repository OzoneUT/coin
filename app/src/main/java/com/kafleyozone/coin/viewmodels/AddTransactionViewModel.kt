package com.kafleyozone.coin.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kafleyozone.coin.R
import com.kafleyozone.coin.data.CategoryRepository
import com.kafleyozone.coin.data.domain.Categories
import com.kafleyozone.coin.utils.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    categoryRepository: CategoryRepository
) : ViewModel() {

    enum class TransactionType {
        INCOME, EXPENSE
    }

    val appCategories: Categories = categoryRepository.appCategories

    private val _transactionType = MutableLiveData<TransactionType>()
    val transactionType: LiveData<TransactionType>
        get() = _transactionType

    private val _date = MutableLiveData<Long>()
    val dateFormatted: LiveData<String> = Transformations.map(_date) {
        formatDate(it)
    }

    fun initialize(date: Long = Date().time, checkedChipId: Int) {
        _date.value = date
        onUpdateTransactionType(checkedChipId)
        Log.i(javaClass.name, appCategories.toString())
    }

    fun onUpdateTransactionType(checkedChipId: Int) {
        _transactionType.value = when (checkedChipId) {
            R.id.expense_chip -> TransactionType.EXPENSE
            else -> TransactionType.INCOME
        }
    }

    fun updateDate(utcMillis: Long) {
        _date.value = utcMillis
    }

    fun getDateMillis(): Long? {
        return _date.value
    }
}