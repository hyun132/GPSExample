//package com.example.myapplication.ui.login
//
//import android.content.Context
//import android.util.Log
//import okhttp3.Interceptor
//import okhttp3.Response
//import org.koin.java.KoinJavaComponent.inject
//
//class LoginCookieInterceptor : Interceptor {
//    val TAG = "LoginCookieInterceptor"
//    private val applicationContext: Context by inject(Context::class.java)
//
//    /*
//    * 로그인 요청하여 response 성공적으로 받을 경우 헤더에서 쿠키 추출하도록함.
//    * */
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
//        val response = chain.proceed(request)
//
//        when (response.code) {
//            200 -> {
//                Log.d(TAG, "login response Success")
//                Log.d(TAG, response.body.toString())
//                response.headers["Set-Cookie"].let { cookie ->
//                    val sharedPref = applicationContext.getSharedPreferences(
//                        "login-cookie",
//                        Context.MODE_PRIVATE
//                    ) ?: null
//                    sharedPref?.let {
//                        with(it.edit()) {
//                            putString("cookie", cookie)
//                            apply()
//                            Log.d(TAG, "쿠키값 저장 $cookie")
//                        }
//                    }
//                }
//            }
//            else -> Log.d(TAG, "login Fail")
//        }
//        return response
//    }
//}