package com.example.myapplication.database

import androidx.lifecycle.LiveData
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
    suspend fun insertLocationLog(locationLog: LocationLog)

    @Insert
    suspend fun insertTrackingLog(trackingLog: TrackingLog)

    @Query("select * from location_table where startTime = :startTime")
    fun getLocationLogs(startTime: Date): LiveData<List<LocationLog>>

    @Query("select * from tracking_table order by trackingStartTime desc")
    fun getTrackingLogs(): LiveData<List<TrackingLog>>

    @Query("delete from location_table where startTime = :startTime")
    suspend fun rollbackSavedLocationLog(startTime: Long)
}