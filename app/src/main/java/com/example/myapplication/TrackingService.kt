package com.example.myapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.LocationLog
import com.example.myapplication.ui.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class TrackingService : Service() {

    var isServiceRunnig = false
    val TAG = "TrackingService"

    val trackingLogToSave = MutableLiveData<MutableList<LocationLog>>()

    val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                START_SERVICE -> {
                    if (!isServiceRunnig) {
                        Log.d(TAG, "service running")
                        createNotificationAndStartService()
                        isServiceRunnig = true
                    }
                }
                STOP_SERVICE -> {
                    stopForeground(true)
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATIO_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_DEFAULT
            )
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotificationAndStartService() {
        val intent = Intent(this, MainActivity::class.java).also {
            it.action = START_SERVICE
            it.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
//
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var notificationbuilder = NotificationCompat.Builder(this, NOTIFICATIO_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("app running")
            .setContentText("테스트")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel()

        startForeground(NOTIFICATIO_ID, notificationbuilder.build())
    }

    @SuppressLint("MissingPermission")
    //처음에 앱 시작할때 권한 요청하기때문에 일단은 보류 -> 추후 해당 프래그먼트 resume될 때 다시 체크하도록 하던지..
    fun trackingLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lng = location.longitude
            }

            // 리스트의 길이 100개넘으면 db에 저장.? 아니면그냥 매번저장..?
            CoroutineScope(Dispatchers.IO).launch {  }
        }


    }

    fun getFormattedCurrentTime(){
        val now = System.currentTimeMillis()


    }

    companion object {
        const val NOTIFICATIO_ID = 1
        const val NOTIFICATIO_CHANNEL_ID = "notificationChannelId"
        const val NOTIFICATION_CHANNEL_NAME = "notificationChannelName"
        val START_SERVICE = "start service"
        val STOP_SERVICE = "stop service"
    }

}