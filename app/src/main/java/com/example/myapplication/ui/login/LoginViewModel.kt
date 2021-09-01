package com.example.myapplication.ui.login

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.LoginRequest
import com.example.myapplication.repsitory.LogInRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: LogInRepository) : ViewModel() {

    private val TAG = "LoginViewModel"
    val isLoginSuccess = MutableLiveData<Boolean>(false)

    val username = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")

    fun login() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (username.value.isNullOrBlank() or password.value
                        .isNullOrBlank()
                ) return@withContext
                repository.login(username.value!!, password.value!!)?.let { response ->
                    response.body()?.let { body ->
                        if (body.code == "200") {
                            isLoginSuccess.postValue(true)
                        } else {
                            Log.d(TAG, "로그인 실패")
                        }
                    }
                } ?: println("응답없음")
            }
        }
    }

    fun checkSession() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                isLoginSuccess.postValue(repository.checkSession())
            }
        }
    }


}