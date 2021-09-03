package com.example.myapplication

import androidx.lifecycle.MutableLiveData

class ServiceStateRepository {
    val isServiceRunning = MutableLiveData<Boolean>(false)
    val currentAddress = MutableLiveData<String>("")
    val currentSpeed = MutableLiveData<String>("0")
    val serviceRunningTime = MutableLiveData<String>("00:00")
    val drivingDistance = MutableLiveData<Float>(0F)
}