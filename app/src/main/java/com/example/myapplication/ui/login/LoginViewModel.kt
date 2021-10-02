package com.example.myapplication.ui.login

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.repsitory.LogInRepository
import com.example.myapplication.ui.base.BaseViewModel

class LoginViewModel(private val repository: LogInRepository) : BaseViewModel() {

    private val TAG = "LoginViewModel"
    val isLoginSuccess = MutableLiveData<Boolean>(false)

    val username = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val errorMessage = MutableLiveData("")

    init {
        checkSession()
    }

    fun login() {
        doIOWork {
            if (username.value.isNullOrBlank() or password.value.isNullOrBlank()
            ) {
                errorMessage.postValue("아이디/비밀번호를 입력해주세요")
            } else {
                repository.login(username.value!!, password.value!!).let { response ->
                    response.body()?.let { body ->
                        if (body.code == "200") {
                            repository.saveCookieFromHeader(response)
                            isLoginSuccess.postValue(true)
                        } else {
                            errorMessage.postValue("아이디/비밀번호를 다시 한번 확인해주세요")
                        }
                    }
                } ?: run {
                    errorMessage.postValue("잠시 후 다시 시도해 주세요.")
                }
            }
        }
    }

    private fun checkSession() {
        doIOWork {
            isLoginSuccess.postValue(repository.checkSession())
        }
    }
}

