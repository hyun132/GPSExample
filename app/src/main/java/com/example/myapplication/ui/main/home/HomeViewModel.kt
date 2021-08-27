package com.example.myapplication.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.LocationLog
import com.example.myapplication.repsitory.LogInRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(val repository: LogInRepository) : ViewModel() {

    private val _currentLocation = MutableLiveData<LatLng>()
//    val savedTrackingList = MutableLiveData<List<TrackingLogWithLocationLog>>()

//    fun getTrackingList() {
//        CoroutineScope(Dispatchers.IO).launch {
//            repository.getSavedTrackingList().map { savedTrackingList.postValue(it) }
//        }
//
//    }

//    fun saveTrackingLocation(location: LocationLog) {
//        CoroutineScope(Dispatchers.IO).launch {
//            repository.saveLocationLogs(location)
//        }
//    }

}