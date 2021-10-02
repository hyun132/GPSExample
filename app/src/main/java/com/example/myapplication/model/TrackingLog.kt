package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/*
* 하나의 주행기록
* */
@Entity(tableName = "tracking_table")
data class TrackingLog(
    @PrimaryKey val trackingStartTime: Long, //기록 시작한 시각
    val trackingEndTime: Long, //기록 종료한 시각
    val trackingDistance: Int
)
