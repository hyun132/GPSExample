package com.example.myapplication.ui.main.drivingroute

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.myapplication.model.LocationLog
import com.example.myapplication.repsitory.TrackingRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class DrivingRouteViewModel(private val repository: TrackingRepository) : ViewModel() {

    private val _locationList = MutableLiveData<List<LatLng>>()
    val locationList: LiveData<List<LatLng>> = _locationList

    lateinit var startTime: Date

    fun getDrivingRoute(startTime: Date) {
        val locationList: LiveData<List<LocationLog>> = repository.getSavedLocationList(startTime)
        val newList = locationList.map { list ->
            list.map {
                LatLng(
                    it.latitude.toDouble(),
                    it.longitude.toDouble()
                )
            }
        }
        _locationList.postValue(newList.value)
    }

//    return Transformations.switchMap { locationList-> { list ->
//        return list.asFlow()
//            .map { locationLog -> LatLng(locationLog.longitude, locationLog.longitude) }
//            .collect()
//    }
//    }

//    fun getDrivingRoute(startTime: Date) {
//        viewModelScope.launch {
//            repository.getSavedLocationList(startTime).collect {
//                _locationList.postValue(it.map {
//                    LatLng(it.latitude.toDouble(), it.longitude.toDouble())
//                })
//            }
//        }
//    }
}