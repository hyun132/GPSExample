package com.example.myapplication.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.repsitory.ServiceStateRepository

class HomeViewModel(serviceStateRepository: ServiceStateRepository) : ViewModel() {

    val TAG = "HomeViewModel"

    val isTrackingStart: LiveData<Boolean> = serviceStateRepository.isServiceRunning

    val currentLocation: LiveData<String> = serviceStateRepository.currentAddress

    val drivingTime: LiveData<String> = serviceStateRepository.serviceRunningTime

    val drivingSpeed: LiveData<String> = serviceStateRepository.currentSpeed

}