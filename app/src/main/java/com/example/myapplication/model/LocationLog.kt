package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* 매 시각마다의 좌표를 시간과 함께 저장.
* */
@Entity(tableName = "location_table")
data class LocationLog(
    @PrimaryKey(autoGenerate = true)
    val locationLogId: Int = 0,
    val latitude: String,
    val longitude: String,
    val startTime: Long
)
