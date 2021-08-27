package com.example.myapplication.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.repsitory.MyRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(repository: MyRepository) : ViewModel() {

    //로그인 결과 처리 어떻게할건지..?
    //자동로그인..? 토큰...?없으면 자동로그인 체크&& 각 et값 다 있을 경우 액티비티 열릴 때 검사해서 로그인.
    fun logIn(username: String, password: String) {

    }

    fun checkSession() {}

}