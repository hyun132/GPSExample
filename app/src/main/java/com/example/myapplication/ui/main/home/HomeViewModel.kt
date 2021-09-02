package com.example.myapplication.ui.main.home

import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.*
import com.example.myapplication.TrackingService
import com.example.myapplication.TrackingService.Companion.currentAddress
import com.example.myapplication.TrackingService.Companion.currentSpeed
import com.example.myapplication.TrackingService.Companion.isServiceRunning
import com.example.myapplication.TrackingService.Companion.serviceRunningTime
import com.example.myapplication.repsitory.LogInRepository
import com.example.myapplication.repsitory.TrackingRepository
import com.google.android.gms.maps.model.LatLng
import org.koin.java.KoinJavaComponent.inject
import java.util.*

class HomeViewModel(
    trackingRepository: TrackingRepository
) : ViewModel() {

    private var _isServiceRunning = isServiceRunning
    val isTrackingStart: LiveData<Boolean>
        get() = _isServiceRunning

    private var _currentAddress = currentAddress
    val currentLocation: LiveData<String>
        get() = _currentAddress

    private var _serviceRunningTime = serviceRunningTime
    val drivingTime: LiveData<String>
        get() = _serviceRunningTime


    private var _currentSpeed = currentSpeed
    val drivingSpeed: LiveData<String>
        get() = _currentSpeed
}