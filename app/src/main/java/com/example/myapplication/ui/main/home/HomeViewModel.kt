package com.example.myapplication.ui.main.home

import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.*
import com.example.myapplication.repsitory.LogInRepository
import com.example.myapplication.repsitory.TrackingRepository
import com.google.android.gms.maps.model.LatLng
import java.util.*

class HomeViewModel(
    trackingRepository: TrackingRepository
) : ViewModel() {

    private val _currentLocation = MutableLiveData<LatLng>()
    var _isServiceRunning = trackingRepository.getTrackingServiceState()
    var isServiceRunning: LiveData<Boolean> = _isServiceRunning

    private var _currentAddress = trackingRepository.getCurrentAddress()
    val currentAddress: LiveData<String>
        get() = _currentAddress

}