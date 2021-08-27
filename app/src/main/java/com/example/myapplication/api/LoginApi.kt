package com.example.myapplication.api

import android.content.SharedPreferences
import com.example.myapplication.model.LoginRequest
import com.example.myapplication.model.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface LoginApi {
    @POST("/api/login")
    @Headers("Content-Type:application/json")
    suspend fun logIn(
        @Body username: LoginRequest,
    ): Response<LoginResponse>


    @Headers(
        "Content-Type:application/json",
        "Charset:UTF-8",
        "GO-Agent:go-Android"
    )
    @GET("/api/alive")
    suspend fun logInWithCookie(
        @Header("Cookie") cookie: String
    ): Response<LoginResponse>


}