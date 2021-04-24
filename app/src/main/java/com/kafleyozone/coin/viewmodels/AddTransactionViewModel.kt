package com.kafleyozone.coin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kafleyozone.coin.utils.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor() : ViewModel() {

    private val _date = MutableLiveData<Long>()
    val dateFormatted: LiveData<String> = Transformations.map(_date) {
        formatDate(it)
    }

    fun initialize(date: Long = Date().time) {
        _date.value = date
    }

    fun updateDate(utcMillis: Long) {
        _date.value = utcMillis
    }

    fun getDateMillis(): Long? {
        return _date.value
    }
}