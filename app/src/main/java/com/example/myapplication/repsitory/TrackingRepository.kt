package com.example.myapplication.repsitory

import androidx.lifecycle.LiveData
import com.example.myapplication.database.TrackingDao
import com.example.myapplication.model.LocationLog
import com.example.myapplication.model.TrackingLog
import java.util.*

class TrackingRepository(private val trackingDao: TrackingDao) {

    suspend fun saveLocationLogs(locationLog: LocationLog) =
        trackingDao.insertLocationLog(locationLog)

    suspend fun insertOrUpdateTrackingLog(trackingLog: TrackingLog) =
        trackingDao.insertOrUpdateTrackingLog(trackingLog)

    fun getSavedLocationList(startTime: Date) = trackingDao.getLocationLogs(startTime)

    fun getSavedTrackingList(): LiveData<List<TrackingLog>> = trackingDao.getTrackingLogs()

}