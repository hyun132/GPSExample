package com.example.myapplication.model

import androidx.databinding.BaseObservable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/*
* 하나의 주행기록
* */
@Entity(tableName = "tracking_table")
data class TrackingLog(
    @PrimaryKey(autoGenerate = true) val trackingId: Int=0,
    val trackingStartTime: Date, //기록 시작한 시각
    val trackingEndTime: Date, //기록 종료한 시각
    val trackingDistance: Int
)
