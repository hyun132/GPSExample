package com.example.myapplication.ui.main.drivingroute

import androidx.lifecycle.*
import com.example.myapplication.model.LocationLog
import com.example.myapplication.repsitory.TrackingRepository
import com.example.myapplication.ui.base.BaseViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DrivingRouteViewModel(private val repository: TrackingRepository) : BaseViewModel() {

    val startTime = MutableLiveData<Date>()

    fun getDrivingRoute(startTime: Date): LiveData<List<LatLng>> {
        return repository.getSavedLocationList(startTime).map { list ->
            list.map {
                LatLng(it.latitude.toDouble(), it.longitude.toDouble())
            }
        }
    }
}