package com.example.myapplication.api

import com.example.myapplication.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApi {
    @POST("/api/login")
    @Headers("Content-Type: application/json")
    suspend fun logIn(
        username: String,
        password: String,
        locale: String = "ko",
        deviceId: String = "test"
    ): Response<LoginResponse>
}