package com.example.myapplication.repsitory

import androidx.lifecycle.MutableLiveData

/* Tracking Service의 상태값을 가지고 있는 객체 */
class ServiceStateRepository {
    val isServiceRunning = MutableLiveData<Boolean>(false)
    val currentAddress = MutableLiveData<String>("")
    val currentSpeed = MutableLiveData<String>("0")
    val serviceRunningTime = MutableLiveData<String>("00:00")
    val drivingDistance = MutableLiveData<Float>(0F)
}