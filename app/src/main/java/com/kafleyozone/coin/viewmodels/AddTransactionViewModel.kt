package com.kafleyozone.coin.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kafleyozone.coin.R
import com.kafleyozone.coin.data.CategoryRepository
import com.kafleyozone.coin.data.domain.Category
import com.kafleyozone.coin.utils.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val TAG = "AddTransactionViewModel"

    enum class TransactionType {
        INCOME, EXPENSE
    }

    private var _categories = listOf<Category>()
    private val _filteredCategories = MutableLiveData<MutableList<Category>>()
    val filteredCategories: MutableLiveData<MutableList<Category>>
        get() = _filteredCategories

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

    fun initializeLists(transactionType: TransactionType?) = updateFilteredList(
        if (transactionType == TransactionType.EXPENSE) {
            _categories = categoryRepository.appCategories.expenseCategories
            categoryRepository.appCategories.expenseCategories
        } else {
            _categories = categoryRepository.appCategories.incomeCategories
            categoryRepository.appCategories.incomeCategories
        }
    )

    private fun updateFilteredList(newList: List<Category>?) {
        newList?.let {
            _filteredCategories.value = newList.toMutableList()
        }
    }

    fun doSearch(query: CharSequence?) {
        Log.i(TAG, "_categories: $_categories")
        query?.let { it ->
            val q = it.toString().toLowerCase(Locale.ROOT)
            if (q.isBlank()) {
                updateFilteredList(_categories)
            } else {
                val filtered: MutableList<Category> = mutableListOf()
                _categories.forEach { category ->
                    Log.i(TAG, "Found query \"$q\" in ${category.categoryName}: " +
                            "${category.categoryName.toLowerCase(Locale.ROOT).contains(q)}")
                    if (category.categoryName.toLowerCase(Locale.ROOT).contains(q)) {
                        filtered.add(category)
                    }
                }
                updateFilteredList(filtered)
            }
        }
    }
}