package com.example.myapplication.repsitory

import com.example.myapplication.api.LoginService.Companion.loginApi
import com.example.myapplication.database.TrackingDao
import com.example.myapplication.model.LocationLog
import com.example.myapplication.model.LoginRequest
import com.example.myapplication.model.LoginResponse
import com.example.myapplication.model.TrackingLog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.io.IOException
import java.util.*

class TrackingRepository(private val trackingDao: TrackingDao) {

    fun saveLocationLogs(locationLog: LocationLog) = trackingDao.insertLocationLog(locationLog)

    fun saveTrackingLogs(trackingLog: TrackingLog) = trackingDao.insertTrackingLog(trackingLog)

    fun getSavedLocationList(startTime: Date) = trackingDao.getLocationLogs(startTime)

    fun getSavedTrackingList() = trackingDao.getTrackingLogs()
}