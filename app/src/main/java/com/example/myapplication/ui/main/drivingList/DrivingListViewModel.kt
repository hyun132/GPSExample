package com.example.myapplication.ui.main.drivingList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.convertMeterToKm
import com.example.myapplication.model.TrackingLog
import com.example.myapplication.repsitory.TrackingRepository
import com.example.myapplication.ui.base.BaseViewModel

class DrivingListViewModel(private val repository: TrackingRepository) : BaseViewModel() {

    private val _drivingList: MediatorLiveData<List<TrackingLog>> =
        MediatorLiveData<List<TrackingLog>>()
    val drivingList: LiveData<List<TrackingLog>> = _drivingList

    private var _drivingDistance = MutableLiveData("0")
    val drivingDistance: LiveData<String> = _drivingDistance

    fun getTotalDrivingDistance() {
        var sum = 0
        drivingList.value?.onEach { sum += it.trackingDistance }
        _drivingDistance.postValue(convertMeterToKm(sum.toFloat()))
    }

    fun getDrivingList() {
        _drivingList.addSource(repository.getSavedTrackingList()) {
            doIOWork {
                _drivingList.postValue(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _drivingList.removeSource(repository.getSavedTrackingList())
    }
}