package com.example.myapplication.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class HomeViewModel:ViewModel() {

    private val _currentLocation = MutableLiveData<LatLng>()


}