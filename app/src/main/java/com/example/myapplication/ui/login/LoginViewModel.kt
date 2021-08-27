package com.example.myapplication.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.MyApplication.Companion.getApplicationContext
import com.example.myapplication.model.LoginRequest
import com.example.myapplication.model.LoginResponse
import com.example.myapplication.repsitory.LogInRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(val repository: LogInRepository) : ViewModel() {

    private val TAG = "LoginViewModel"
    val isLoginSuccess = MutableLiveData<Boolean>(false)

    /*
    * 로그인 요청하여 response 성공적으로 받을 경우 헤더에서 쿠키 추출하도록함.
    * */
    fun logIn(loginRequest: LoginRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "로그인 요청")
            repository.logIn(loginRequest)?.let { response ->
                response.body()?.let { body ->
                    if (body.code == "200") {
                        saveCookieFromHeader(response)
                        Log.d(TAG, "로그인 성공")
                    } else {
                        Log.d(TAG, "로그인 실패")
                    }
                }
            } ?: println("응답없음")

        }
    }

    /*
    * 헤더에서 쿠키 정보 얻어 sharedPreference에 저장
    * */
    fun saveCookieFromHeader(response: Response<LoginResponse>) {
        //쿠키값중 마지막GOSSOcookie를 사용함. get에서 해당 이름의 마지막값가져옴. 그냥 저장해도될듯.
        response.headers()["Set-Cookie"].let { cookie ->
            val sharedPref = getApplicationContext().getSharedPreferences(
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
            isLoginSuccess.postValue(true)
        }
    }

    // 세션 로그인부분은 api정상화되면 구현
    fun checkSession() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref =
                getApplicationContext().getSharedPreferences("login-cookie", Context.MODE_PRIVATE)
            sharedPref.getString("cookie", "")?.let {cookie->
                repository.logInWithCookie(cookie).let { response->
                    if (response != null) {
                        if(response.body()?.code =="200"){
                            isLoginSuccess.postValue(true)
                        }
                    }
                }
            }
        }
    }

}