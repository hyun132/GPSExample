package com.example.myapplication.repsitory

import android.content.Context
import android.util.Log
import com.example.myapplication.api.LoginApi
import com.example.myapplication.model.LoginRequest
import com.example.myapplication.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class LogInRepository(
    private val loginApi: LoginApi,
    private val applicationContext: Context
) {
    private val TAG = "LoginRepository"

    /* 아이디와 비밀번호 이용한 로그인 */
    suspend fun login(username: String, password: String): Response<LoginResponse>? {
        var response: Response<LoginResponse>? = null
        try {
            response = loginApi.login(LoginRequest(username, password))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }


    /* 저장된 쿠키 값을 이용한 로그인 (수정필요) */
    private suspend fun logInWithCookie(cookie: String): Response<LoginResponse>? {
        var response: Response<LoginResponse>? = null
        try {
            response = loginApi.loginWithCookie(cookie)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }

    /*
    * 헤더에서 쿠키 정보 얻어 sharedPreference에 저장
    * */
    fun saveCookieFromHeader(response: Response<LoginResponse>) {
        response.headers()["Set-Cookie"].let { cookie ->
            val sharedPref = applicationContext.getSharedPreferences(
                "login-cookie",
                Context.MODE_PRIVATE
            ) ?: null
            sharedPref?.let {
                Log.d(TAG, "쿠키값 받아옴 $cookie")
                with(it.edit()) {
                    putString("cookie", cookie)
                    apply()
                    Log.d(TAG, "쿠키값 저장 $cookie")
                }
            }
        }
    }

    /*
    * sharedPreference에 저장된 쿠키값이 유효한 경우 자동 로그인.
    * 쿠기값 유효성을 만료일로 할지 요청을 보낼지 고민중.
    * */
    suspend fun checkSession(): Boolean {
        val sharedPref =
            applicationContext.getSharedPreferences("login-cookie", Context.MODE_PRIVATE)
        var success = false
        sharedPref.getString("cookie", "")?.let { cookie ->
            logInWithCookie(cookie).let { response ->
                if (response != null) {
                    if (response.body()?.code == "200") {
                        success = true
                    }
                }
            }
        }
        return success
    }
}