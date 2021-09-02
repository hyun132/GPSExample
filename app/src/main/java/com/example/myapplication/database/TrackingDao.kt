package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.model.LocationLog
import com.example.myapplication.model.TrackingLog
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TrackingDao {

    @Insert
    fun insertLocationLog(locationLog: LocationLog)

    @Insert
    fun insertTrackingLog(trackingLog: TrackingLog)

    @Query("select * from location_table where startTime = :startTime")
    fun getLocationLogs(startTime: Date): Flow<List<LocationLog>>

    @Query("select * from tracking_table order by trackingStartTime desc")
    fun getTrackingLogs(): Flow<List<TrackingLog>>

    @Query("delete from location_table where startTime = :startTime")
    fun rollbackSavedLocationLog(startTime: Long)
}