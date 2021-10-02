package com.example.myapplication.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private fun showProgress() {
        _isLoading.postValue(true)
    }

    private fun hideProgress() {
        _isLoading.postValue(false)
    }

    fun doIOWork(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                showProgress()
                block()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                hideProgress()
            }
        }
    }
}