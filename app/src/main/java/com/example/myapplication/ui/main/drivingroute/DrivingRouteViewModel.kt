package com.example.myapplication.ui.main.drivingroute

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.LocationLog
import com.example.myapplication.repsitory.TrackingRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

class DrivingRouteViewModel(private val repository: TrackingRepository) : ViewModel() {

    private val _locationList = MutableLiveData<List<LatLng>>()
    val locationList: LiveData<List<LatLng>>
        get() = _locationList

    lateinit var startTime: Date

    fun getDrivingRoute(startTime: Date) {
        viewModelScope.launch {
            repository.getSavedLocationList(startTime).collect {
                _locationList.postValue(it.map {
                    LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                })
            }
        }
    }
}