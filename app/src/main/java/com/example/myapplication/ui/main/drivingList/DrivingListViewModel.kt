package com.example.myapplication.ui.main.drivingList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.TrackingLog
import com.example.myapplication.repsitory.TrackingRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DrivingListViewModel(private val repository: TrackingRepository) : ViewModel() {

    private val _drivingList = MutableLiveData<List<TrackingLog>>()
    val drivingList: LiveData<List<TrackingLog>>
        get() = _drivingList

    fun getDrivingList() {
        viewModelScope.launch {
            repository.getSavedTrackingList()
                .catch { exception -> exception.printStackTrace() }
                .collect { list -> _drivingList.postValue(list) }
        }
    }
}