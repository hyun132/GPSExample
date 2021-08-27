package com.example.myapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.LocationLog
import com.example.myapplication.ui.main.MainActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.Task

class TrackingService : Service() {

    var isServiceRunnig = false
    val TAG = "TrackingService"

    val trackingLogToSave = MutableLiveData<MutableList<LocationLog>>()
    var currentLocation: Location? = null

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
                    if (isServiceRunnig) {
                        cancelGetLocationPerThreeSecond()
                        stopForeground(true)
                    } else {
                        createNotificationAndStartService()
                        getLocationPerThreeSecond()
                        isServiceRunnig = true
                    }
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

        val notificationbuilder = NotificationCompat.Builder(this, NOTIFICATIO_CHANNEL_ID)
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

    /*
    * 3초당 위치 정보 받아옴
    * 참조 : https://codelabs.developers.google.com/codelabs/while-in-use-location#3
    * */
    @SuppressLint("MissingPermission")
    //처음에 앱 시작할때 권한 요청하기때문에 일단은 보류 -> 추후 해당 프래그먼트 resume될 때 다시 체크하도록 하던지..
    fun getLocationPerThreeSecond() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
            }
        }

        val locationRequest = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 2000
            priority = PRIORITY_HIGH_ACCURACY // PRIORITY_HIGH_ACCURACY 는 가장 정확한 위치정보
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            //Looper에 대해서 조금 더 알아볼것. 일단 메인루퍼로 처리.
            //위치정보 변경사항 받아옴
            Log.d(TAG, "service running")
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                //위치 못읽어오는 상태. 다이얼로그 띄워서 위치 설정해주도록 유도해야함.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().

                    //만약 설정 요청을 해야한다면 액티비티/프래그먼트에 intent 보내서 다이얼로그 띄워주어야할까?
                    //다른 방법 있는지 확인해볼것.

//                    exception.startResolutionForResult(this,
//                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            currentLocation = locationResult.lastLocation

            // 일단 위치정보 받아오면 로그로찍어보기
            Log.d(
                TAG,
                "in locationCallback : currentLcation -> lat:${locationResult.lastLocation.latitude}, lng:${locationResult.lastLocation.longitude}"
            )
//                // 리스트의 길이 100개넘으면 db에 저장.? 아니면그냥 매번저장..?
//                CoroutineScope(Dispatchers.IO).launch {
//
//                }
        }
    }

    fun cancelGetLocationPerThreeSecond() {
        val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        removeTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Location Callback removed.")
                stopSelf()
            } else {
                Log.d(TAG, "Failed to remove Location Callback.")
            }
        }
    }

    fun getFormattedCurrentTime() {
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