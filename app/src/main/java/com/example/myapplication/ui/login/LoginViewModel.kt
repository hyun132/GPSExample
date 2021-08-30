package com.example.myapplication.ui.login

import android.util.Log
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

    /*
    * 로그인 요청하여 response 성공적으로 받을 경우 헤더에서 쿠키 추출하도록함.
    * */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            repository.login(username, password)?.let { response ->
                response.body()?.let { body ->
                    if (body.code == "200") {
                        repository.saveCookieFromHeader(response)
                        Log.d(TAG, "로그인 성공")
                        isLoginSuccess.postValue(true)
                    } else {
                        Log.d(TAG, "로그인 실패")
                    }
                }
            } ?: println("응답없음")
        }
    }

    fun checkSession() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                isLoginSuccess.postValue(repository.checkSession())
            }


        }
    }
}