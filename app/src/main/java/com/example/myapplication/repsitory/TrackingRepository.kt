package com.example.myapplication.repsitory

import com.example.myapplication.TrackingService
import com.example.myapplication.TrackingService.Companion.currentAddress
import com.example.myapplication.TrackingService.Companion.isServiceRunning
import com.example.myapplication.database.TrackingDao
import com.example.myapplication.model.LocationLog
import com.example.myapplication.model.TrackingLog
import java.util.*

class TrackingRepository(
    private val trackingDao: TrackingDao,
    private val trackingService: TrackingService
) {

    fun saveLocationLogs(locationLog: LocationLog) = trackingDao.insertLocationLog(locationLog)

    fun saveTrackingLogs(trackingLog: TrackingLog) = trackingDao.insertTrackingLog(trackingLog)

    fun getSavedLocationList(startTime: Date) = trackingDao.getLocationLogs(startTime)

    fun getSavedTrackingList() = trackingDao.getTrackingLogs()

    fun getTrackingServiceState() = isServiceRunning

    fun getCurrentAddress() = currentAddress

}