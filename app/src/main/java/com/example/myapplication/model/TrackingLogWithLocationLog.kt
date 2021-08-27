//package com.example.myapplication.model
//
//import androidx.room.Embedded
//import androidx.room.Relation
//
//data class TrackingLogWithLocationLog(
//    @Embedded val trackingLog:TrackingLog,
//    @Relation(
//        parentColumn = "trackingStartTime",
//        entityColumn = "startTime"
//    )
//    val locationLog:List<LocationLog>
//)
