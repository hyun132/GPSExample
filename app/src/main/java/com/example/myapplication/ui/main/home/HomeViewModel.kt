package com.example.myapplication.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.ServiceStateRepository
import com.example.myapplication.TrackingService
import org.koin.java.KoinJavaComponent.inject

class HomeViewModel(serviceStateRepository: ServiceStateRepository) : ViewModel() {

    //    val trackingService :TrackingService by inject(TrackingService::class.java)

    val TAG = "HomeViewModel"

    //    private var _isServiceRunning
    val isTrackingStart: LiveData<Boolean> = serviceStateRepository.isServiceRunning
//        get() = _isServiceRunning

    //    private var _currentAddress
    val currentLocation: LiveData<String> = serviceStateRepository.currentAddress
//        get() = _currentAddress

    //    private var _serviceRunningTime
    val drivingTime: LiveData<String> = serviceStateRepository.serviceRunningTime
//        get() = _serviceRunningTime


    //    private var _currentSpeed = trackingService.currentSpeed
    val drivingSpeed: LiveData<String> = serviceStateRepository.currentSpeed
//        get() = _currentSpeed

}