package com.example.myapplication.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.repsitory.ServiceStateRepository
import com.example.myapplication.ui.base.BaseViewModel

class HomeViewModel(serviceStateRepository: ServiceStateRepository) : BaseViewModel() {

    val isTrackingStart: LiveData<Boolean> = serviceStateRepository.isServiceRunning
    val currentLocation: LiveData<String> = serviceStateRepository.currentAddress
    val drivingTime: LiveData<String> = serviceStateRepository.serviceRunningTime
    val drivingSpeed: LiveData<String> = serviceStateRepository.currentSpeed
}