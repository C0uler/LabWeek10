package com.jordan.labweek10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel : ViewModel() {


    private val _total = MutableLiveData<Int>()

    val total: LiveData<Int>
        get() = _total


    init {
        _total.value = 0
    }


    fun incrementTotal() {
        // This will automatically notify any observers.
        _total.value = (_total.value ?: 0) + 1
    }

    fun setTotal(newTotal: Int) {
        _total.postValue(newTotal)
    }
}