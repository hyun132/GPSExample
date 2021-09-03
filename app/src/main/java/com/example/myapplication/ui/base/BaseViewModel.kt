package com.example.myapplication.ui.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun showProgress() {
        _isLoading.postValue(true)
    }

    fun hideProgress() {
        _isLoading.postValue(false)
        Log.d("LoginViewModel", "hide progressBar!!!!!!")
    }

    /* IO작업 하는 동안 progressbar 띄워주기 */
    fun dataLoad(block: suspend () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    showProgress()
                    block()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    hideProgress()
                }
            }
        }
    }
}