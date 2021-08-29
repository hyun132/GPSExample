package com.example.myapplication.repsitory

import com.example.myapplication.api.LoginService.Companion.loginApi
import com.example.myapplication.database.TrackingDao
import com.example.myapplication.model.LocationLog
import com.example.myapplication.model.LoginRequest
import com.example.myapplication.model.LoginResponse
import com.example.myapplication.model.TrackingLog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.io.IOException

class LogInRepository(private val trackingDao: TrackingDao) {

    /* 아이디와 비밀번호 이용한 로그인 */
    suspend fun logIn(loginRequest: LoginRequest): Response<LoginResponse>? {
        var response: Response<LoginResponse>? = null
        try {
            response = loginApi.logIn(loginRequest)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }

    /* 저장된 쿠키 값을 이용한 로그인 (수정필요) */
    suspend fun logInWithCookie(cookie: String): Response<LoginResponse>? {
        var response: Response<LoginResponse>? = null
        try {
            response = loginApi.logInWithCookie(cookie)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }
}