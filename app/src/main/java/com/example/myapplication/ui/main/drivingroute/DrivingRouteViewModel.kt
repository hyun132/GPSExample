package com.example.myapplication.ui.main.drivingroute

import androidx.lifecycle.*
import com.example.myapplication.repsitory.TrackingRepository
import com.example.myapplication.ui.base.BaseViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.*

class DrivingRouteViewModel(private val repository: TrackingRepository) : BaseViewModel() {

    fun getDrivingRoute(startTime: Long): LiveData<List<LatLng>> =
        repository.getSavedLocationList(startTime).map { list ->
            list.map {
                LatLng(it.latitude.toDouble(), it.longitude.toDouble())
            }
        }
}