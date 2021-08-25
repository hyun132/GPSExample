package com.example.myapplication.model

/*
* 하나의 주행기록
* */
data class TrackingLog(
    val dateTime:String, //기록 시작한 시각
    val latlngLog:LocationLog
)
