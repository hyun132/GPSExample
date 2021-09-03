package com.example.myapplication.ui.main.drivingList

import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.calculateDistance
import com.example.myapplication.model.TrackingLog
import com.example.myapplication.repsitory.TrackingRepository
import com.example.myapplication.ui.base.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.internal.format
import java.text.DecimalFormat
import java.util.*

class DrivingListViewModel(private val repository: TrackingRepository) : ViewModel() {

    private val _drivingList = MutableLiveData<List<TrackingLog>>()
    val drivingList: LiveData<List<TrackingLog>> = repository.getSavedTrackingList()

    private var _drivingDistance = MutableLiveData("")
    val drivingDistance: LiveData<String>
        get() = _drivingDistance

    fun getTotalDrivingDistance() {
        var sum = 0
        drivingList.value?.onEach { sum += it.trackingDistance }
        _drivingDistance.postValue("%.2f".format(sum / 1000f))
    }

}