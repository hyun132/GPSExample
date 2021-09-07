package com.example.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.model.LocationLog
import com.example.myapplication.model.TrackingLog
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TrackingDao {

    @Insert
    suspend fun insertLocationLog(locationLog: LocationLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateTrackingLog(trackingLog: TrackingLog)

    @Query("select * from location_table where startTime = :startTime")
    fun getLocationLogs(startTime: Long): LiveData<List<LocationLog>>

    @Query("select * from tracking_table order by trackingStartTime desc")
    fun getTrackingLogs(): LiveData<List<TrackingLog>>

}