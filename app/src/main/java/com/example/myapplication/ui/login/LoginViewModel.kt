package com.example.myapplication.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.repsitory.LogInRepository
import com.example.myapplication.ui.base.BaseViewModel

class LoginViewModel(private val repository: LogInRepository) : BaseViewModel() {

    private val TAG = "LoginViewModel"
    val isLoginSuccess = MutableLiveData<Boolean>(false)

    val username = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")

    fun login() {
        dataLoad {
            if (username.value.isNullOrBlank() or password.value
                    .isNullOrBlank()
            ) return@dataLoad
            repository.login(username.value!!, password.value!!)?.let { response ->
                response.body()?.let { body ->
                    if (body.code == "200") {
                        repository.saveCookieFromHeader(response)
                        isLoginSuccess.postValue(true)
                    } else {
                        Log.d(TAG, "로그인 실패")
                    }
                }
            } ?: println("응답없음")
        }
    }

    fun checkSession() {
        dataLoad { isLoginSuccess.postValue(repository.checkSession()) }
    }

}

