package com.example.myapplication.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginService {

    private val LOGIN_URL = "https://nstaging.daouoffice.com"

    private val retrofit: Retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
//            .addInterceptor(LoginCookieInterceptor())
            .build()

        Retrofit.Builder()
            .baseUrl(LOGIN_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val loginApi: LoginApi = retrofit.create(LoginApi::class.java)
}