package com.example.myapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import com.example.myapplication.model.LocationLog
import com.example.myapplication.model.TrackingLog
import com.example.myapplication.repsitory.TrackingRepository
import com.example.myapplication.ui.main.MainActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.roundToInt

class TrackingService : LifecycleService() {
    val trackingRepository: TrackingRepository by inject()

    val TAG = "TrackingService"

    lateinit var notificationManager: NotificationManager
    lateinit var timerTask: Timer

    private val _serviceRunningTime = MutableLiveData<String>("00:00")
    val serviceRuntime: LiveData<String>
        get() = _serviceRunningTime

    private val _drivingDistance = MutableLiveData<Float>(0F)
    val drivingDistance: LiveData<Float>
        get() = _drivingDistance

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    var lastLocation = MutableLiveData(Location("").apply {
        latitude = 0.0
        longitude = 0.0
    }
    )
    val startTime by lazy { System.currentTimeMillis() }
    //일단 테스트위해서

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        lifecycleScope.launch {
            intent?.let {
                when (it.action) {
                    START_SERVICE -> {
                        startOrStopService()
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private suspend fun startOrStopService() {
        if (isServiceRunning.value == true) {
            cancelGetLocationPerThreeSecond()
            //db에 주행기록 저장.
            withContext(Dispatchers.IO) {
                trackingRepository.saveTrackingLogs(
                    TrackingLog(
                        trackingStartTime = Date(startTime),
                        trackingEndTime = Date(System.currentTimeMillis()),
                        trackingDistance = drivingDistance.value!!.roundToInt() //m단위
                    )
                )
                isServiceRunning.postValue(false)
            }
            stopTimer()
            stopForeground(true)
        } else {
            createNotificationAndStartService()
            withContext(Dispatchers.IO) {
                getLocationPerThreeSecond()
                startTimer()
            }
            isServiceRunning.postValue(true)
//            getCurrentAddress()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATIO_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_DEFAULT
            )
            channel.setSound(null, null)
            channel.enableVibration(false)
            // Register the channel with the system
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationAndStartService() {

        val intent = Intent(this, MainActivity::class.java).also {
            it.action = START_SERVICE
            it.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATIO_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("주행거리 측정중")
            .setContentText("00:00")
            .setSmallIcon(R.drawable.ic_driving)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel()

        startForeground(NOTIFICATIO_ID, notificationBuilder.build())

        serviceRuntime.observe(this, { notifyTimeChanged(notificationBuilder, it) })
    }

    /*
    * 3초당 위치 정보 받아옴
    * 참조 : https://codelabs.developers.google.com/codelabs/while-in-use-location#3
    * */
    @SuppressLint("MissingPermission")
    //처음에 앱 시작할때 권한 요청하기때문에 일단은 보류 -> 추후 해당 프래그먼트 resume될 때 다시 체크하도록 하던지..
    fun getLocationPerThreeSecond() {
//        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
//            lastLocation.postValue(location)
//        }
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
            //위치정보 변경사항 받아옴
            Log.d(TAG, "service running")
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()!!
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

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            val newLocation = locationResult.lastLocation

            // 일단 위치정보 받아오면 로그로찍어보기
            Log.d(
                TAG,
                "in locationCallback : currentLcation -> lat:${newLocation.latitude}, lng:${newLocation.longitude}"
            )
            Log.d(TAG, "service State -> active : ${isServiceRunning.value}")

            lastLocation.value?.let { location ->
                if (location.latitude == 0.0 && location.longitude == 0.0) {
                    lastLocation.postValue(newLocation)
                } else {
                    _drivingDistance.postValue(
                        _drivingDistance.value?.plus(
                            calculateDistance(
                                location,
                                newLocation
                            )
                        )
                    )
                    lastLocation.postValue(newLocation)
                    getCurrentAddress(newLocation)
                }
            }


            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    trackingRepository.saveLocationLogs(
                        LocationLog(
                            latitude = locationResult.lastLocation.latitude.toString(),
                            longitude = locationResult.lastLocation.longitude.toString(),
                            startTime = startTime
                        )
                    )
                }
            }
        }
    }

    private fun cancelGetLocationPerThreeSecond() {
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

    private fun startTimer() {
        timer(period = TIMER_INTERVAL, initialDelay = TIMER_INTERVAL) {
            val date = Date(System.currentTimeMillis())
            _serviceRunningTime.postValue(
                Date(startTime).getTakenTime(date)
            )
        }.also { timerTask = it }
    }

    private fun stopTimer() {
        timerTask.cancel()
    }

    private fun notifyTimeChanged(builder: NotificationCompat.Builder, time: String) {
        builder.apply {
            setContentText(currentAddress.value)
            setContentTitle("주행거리 기록중 $time")
        }
        notificationManager.notify(NOTIFICATIO_ID, builder.build())
    }


    /*
    * 현재위치를 주소로 변환
    * */
    fun getCurrentAddress(location: Location) {
        val geocoder = Geocoder(applicationContext, Locale.KOREA)
        Log.d("address", "${location.latitude}, ${location.longitude}")
        val address =
            geocoder.getFromLocation(location.latitude, location.longitude, 1)?.let {
                it[0]?.getAddressLine(0)
            }
        Log.d("address", "${location.latitude}, ${location.longitude} , ${address ?: "주소없음."}")
        address?.let { currentAddress.postValue(it) }
    }

    companion object {
        const val NOTIFICATIO_ID = 1
        const val NOTIFICATIO_CHANNEL_ID = "notificationChannelId"
        const val NOTIFICATION_CHANNEL_NAME = "notificationChannelName"
        const val TIMER_INTERVAL = 1000L
        val START_SERVICE = "start service"
        val STOP_SERVICE = "stop service"
        var isServiceRunning = MutableLiveData<Boolean>(false)
        var currentAddress = MutableLiveData<String>("")
    }

}