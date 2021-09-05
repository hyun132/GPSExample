package com.example.myapplication.ui.main.drivingroute

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.myapplication.repsitory.TrackingRepository
import com.example.myapplication.ui.base.BaseViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.*

class DrivingRouteViewModel(private val repository: TrackingRepository) : BaseViewModel() {

    private val _drivingRoute: MediatorLiveData<List<LatLng>> = MediatorLiveData<List<LatLng>>()
    val drivingRoute: LiveData<List<LatLng>> = _drivingRoute

    fun getDrivingRoute(startTime: Date) {
        doIOWork {
            _drivingRoute.addSource(repository.getSavedLocationList(startTime)) { list ->
                _drivingRoute.postValue(list.map {
                    LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                })
            }
        }
    }
}