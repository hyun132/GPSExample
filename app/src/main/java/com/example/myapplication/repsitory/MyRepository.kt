package com.example.myapplication.repsitory

import com.example.myapplication.api.LoginService.Companion.loginApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.io.IOException

class MyRepository(ioDispatcher:CoroutineDispatcher = Dispatchers.IO) {

    //그냥 값 한개씩 넘겨주는게 낫나..
    suspend fun logIn(username:String,password:String){

        try {
            loginApi.logIn(username,password)
        }catch (e:IOException){

        }
    }
}