package com.example.myapplication.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.TrackingService
import com.example.myapplication.TrackingService.Companion.currentAddress
import com.example.myapplication.TrackingService.Companion.currentSpeed
import com.example.myapplication.TrackingService.Companion.isServiceRunning
import com.example.myapplication.TrackingService.Companion.serviceRunningTime
import org.koin.java.KoinJavaComponent.inject

class HomeViewModel : ViewModel() {

//    val trackingService :TrackingService by inject(TrackingService::class.java)

    val TAG = "HomeViewModel"

//    private var _isServiceRunning
    val isTrackingStart: LiveData<Boolean> = isServiceRunning
//        get() = _isServiceRunning

//    private var _currentAddress
    val currentLocation: LiveData<String> = currentAddress
//        get() = _currentAddress

//    private var _serviceRunningTime
    val drivingTime: LiveData<String> = serviceRunningTime
//        get() = _serviceRunningTime


//    private var _currentSpeed = trackingService.currentSpeed
    val drivingSpeed: LiveData<String> = currentSpeed
//        get() = _currentSpeed

}