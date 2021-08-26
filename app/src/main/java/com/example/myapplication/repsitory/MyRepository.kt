package com.example.myapplication.repsitory

import com.example.myapplication.api.LoginService.Companion.loginApi
import com.example.myapplication.model.LoginRequest
import com.example.myapplication.model.LoginResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.io.IOException

class MyRepository() {

    //그냥 값 한개씩 넘겨주는게 낫나..
    suspend fun logIn(loginRequest:LoginRequest): Response<LoginResponse>? {
        var response: Response<LoginResponse>? = null
        try {
            response = loginApi.logIn(loginRequest)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun logInWithCookie(cookie: String) {
        loginApi.logInWithCookie(cookie)
    }
}