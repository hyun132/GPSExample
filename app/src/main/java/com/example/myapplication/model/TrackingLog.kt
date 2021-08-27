package com.example.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/*
* 하나의 주행기록
* */
@Entity(tableName = "tracking_table")
data class TrackingLog(
    @PrimaryKey(autoGenerate = true)
    val trackingId: Int,
    val trackingStartTime: Date, //기록 시작한 시각
)
