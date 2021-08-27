package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.model.LocationLog
import com.example.myapplication.model.TrackingLog
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackingDao {

//    @Query("select * from tracking_table")
//    fun getAll():Flow<List<TrackingLogWithLocationLog>>
//
//    @Query("select * from tracking_table")
//    fun getLocationLog(key:Long):TrackingLogWithLocationLog

    @Insert
    fun insertLocationLog(locationLog: LocationLog)

    @Insert
    fun insertTrackingLog(trackingLog:TrackingLog)

    @Query("select * from location_table where startTime = :startTime")
    fun getLocationLogs(startTime:Long): Flow<List<LocationLog>>
}