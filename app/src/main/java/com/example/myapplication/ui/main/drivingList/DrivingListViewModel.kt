package com.example.myapplication.ui.main.drivingList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.convertMeterToKm
import com.example.myapplication.repsitory.TrackingRepository
import com.example.myapplication.ui.base.BaseViewModel

class DrivingListViewModel(private val repository: TrackingRepository) : BaseViewModel() {

    val drivingList = repository.getSavedTrackingList()

    private var _drivingDistance = MutableLiveData("0")
    val drivingDistance: LiveData<String> = _drivingDistance

    fun getTotalDrivingDistance() {
        var sum = 0
        drivingList.value?.onEach { sum += it.trackingDistance }
        _drivingDistance.postValue(convertMeterToKm(sum.toFloat()))
    }
}