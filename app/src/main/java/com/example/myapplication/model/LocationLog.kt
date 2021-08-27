package com.example.myapplication.model

import com.google.android.gms.maps.model.LatLng

/*
* 매 시각마다의 좌표를 시간과 함께 저장.
* */
data class LocationLog(
    val latlng: LatLng,
    val time: String
)
