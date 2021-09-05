package com.example.myapplication.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import com.example.myapplication.*
import com.example.myapplication.R
import com.example.myapplication.model.LocationLog
import com.example.myapplication.model.TrackingLog
import com.example.myapplication.repsitory.ServiceStateRepository
import com.example.myapplication.repsitory.TrackingRepository
import com.example.myapplication.ui.main.MainActivity
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.roundToInt

/*
* 3초당 위치 정보를 받아와 속도, 주소값을 생성해주고 각 정보를 데이터베이스에 저장
* */
class TrackingService : LifecycleService() {
    private val trackingRepository: TrackingRepository by inject()
    private val serviceStateRepository: ServiceStateRepository by inject()

    val TAG = "TrackingService"
    lateinit var notificationManager: NotificationManager
    lateinit var timerTask: Timer
    val startTime by lazy { System.currentTimeMillis() }

    var lastLocation = MutableLiveData(Location("").apply { latitude = 0.0; longitude = 0.0 })
    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "service onDestroy")
        stopForeground(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        lifecycleScope.launch {
            intent?.let {
                when (it.action) {
                    START_SERVICE -> {
                        Log.d(TAG, "service START_SERVICE")
                        startTrackingService()
                    }
                    STOP_SERVICE -> {
                        stopTrackingService()
                    }
                    else -> Log.d(TAG, "no_such_intent_action")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    /*
    * 서비스를 종료하기 위해 각 기능 중지 및 데이터 저장/초기화.
    * */
    private fun stopTrackingService() {
        if (serviceStateRepository.isServiceRunning.value == true) {
            cancelGetLocationPerThreeSecond()
            saveTrackingLog()
            stopTimer()
            initializeStateValue()
            serviceStateRepository.isServiceRunning.postValue(false)
        }
    }

    private fun startTrackingService() {
        if (serviceStateRepository.isServiceRunning.value == false) {
            createNotificationAndStartService()
            getLocationPerThreeSecond()
            startTimer()
            saveTrackingLog()
            serviceStateRepository.isServiceRunning.postValue(true)
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
            it.action = RESUME_SERVICE
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT)

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

        serviceStateRepository.serviceRunningTime.observe(
            this,
            { notifyTimeChanged(notificationBuilder, it) })
    }

    /*
    * 3초당 위치 정보 받아옴
    * 참조 : https://codelabs.developers.google.com/codelabs/while-in-use-location#3
    * */
    @SuppressLint("MissingPermission")
    //처음에 앱 시작할때 권한 요청하기때문에 일단은 보류 -> 추후 해당 프래그먼트 resume될 때 다시 체크하도록 하던지..
    fun getLocationPerThreeSecond() {
        CoroutineScope(Dispatchers.Default).launch {
            val locationRequest = LocationRequest.create().apply {
                interval = 3000
                fastestInterval = 2000
                priority = PRIORITY_HIGH_ACCURACY // PRIORITY_HIGH_ACCURACY 는 가장 정확한 위치정보
            }
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            val client: SettingsClient =
                LocationServices.getSettingsClient(this@TrackingService)
            val task: Task<LocationSettingsResponse> =
                client.checkLocationSettings(builder.build())

            task.addOnSuccessListener {
                // All location settings are satisfied. The client can initialize
                Log.d(TAG, "service running")
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()!!
                )
            }

            task.addOnFailureListener { exception ->
                Toast.makeText(
                    this@TrackingService,
                    "위치정보를 가져올 수 없습니다. 위치설정을 해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
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
            updateServiceStateValues(lastLocation, newLocation)
            saveLocationLog(locationResult)
            saveTrackingLog()
        }
    }

    /*
    * 받아온 위치 정보 기반으로 주소, 속도, 등의 값 없데이트
    * */
    fun updateServiceStateValues(lastLocation: MutableLiveData<Location>, newLocation: Location) {
        lastLocation.value?.let { location ->
            if (location.latitude == 0.0 && location.longitude == 0.0) {
                lastLocation.postValue(newLocation)
            } else {
                calculateDistance(location, newLocation).let {
                    serviceStateRepository.drivingDistance.postValue(
                        serviceStateRepository.drivingDistance.value?.plus(it)
                    )
                    serviceStateRepository.currentSpeed.postValue(
                        convertMeterToKm(it * 1200)
                    )
                }
                lastLocation.postValue(newLocation)
                getCurrentAddress(newLocation)
            }
        }
    }

    /*
    * 위치정보 리스너 제거 및 서비스 종료
    * */
    private fun cancelGetLocationPerThreeSecond() {
        val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        removeTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Location Callback removed.")
            } else {
                Log.d(TAG, "Failed to remove Location Callback.")
            }
            stopSelf()
        }
    }

    /*
    * 타이머시작.
    * */
    private fun startTimer() {
        timer(period = TIMER_INTERVAL, initialDelay = TIMER_INTERVAL) {
            val date = Date(System.currentTimeMillis())
            serviceStateRepository.serviceRunningTime.postValue(
                Date(startTime).getTakenTime(date)
            )
            Log.d(TAG, "timer is still working.. in ${Thread.currentThread().name}")
        }.also { timerTask = it }
    }

    private fun stopTimer() {
        timerTask.cancel()
    }

    /*
    * notification의 주행시간과 현재위치를 업데이트.
    * */
    private fun notifyTimeChanged(builder: NotificationCompat.Builder, time: String) {
        builder.apply {
            setContentText(serviceStateRepository.currentAddress.value)
            setContentTitle("주행거리 기록중 $time")
        }
        notificationManager.notify(NOTIFICATIO_ID, builder.build())
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "service onCreate")
    }

    /*
    * 현재위치를 주소로 변환
    * */
    private fun getCurrentAddress(location: Location) {
        lifecycleScope.launch {
            val geocoder = Geocoder(applicationContext, Locale.KOREA)
            withContext(Dispatchers.IO) {
                try {
                    serviceStateRepository.currentAddress.postValue(
                        geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )[0]?.getAddressLine(0)
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /*
    * 비정상종료를 대비해 TrackingLog를 db에 저장해두고 업데이트
    * */
    private fun saveTrackingLog() {
        Log.d(TAG, "trackinflog saved") // main
        CoroutineScope(Dispatchers.IO).launch { //새로운 스코프 만들어 서비스 종료되어도 저장은 완료할 수 있게
            serviceStateRepository.drivingDistance.value?.let {
                TrackingLog(
                    trackingStartTime = Date(startTime),
                    trackingEndTime = Date(System.currentTimeMillis()),
                    trackingDistance = it.roundToInt() //m단위
                )
            }?.let { trackingRepository.insertOrUpdateTrackingLog(it) }
        }
    }

    /*
     * 파라미터로 받은 위치정보를 db에 저장
     * */
    private fun saveLocationLog(locationResult: LocationResult) {
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

    /*
    * 모든 상태데이터 초기화
    * */
    private fun initializeStateValue() {
        serviceStateRepository.apply {
            isServiceRunning.postValue(false)
            currentAddress.postValue("")
            currentSpeed.postValue("0")
            serviceRunningTime.postValue("00:00")
            drivingDistance.postValue(0F)
        }
    }

    companion object {
        const val NOTIFICATIO_ID = 1
        const val NOTIFICATIO_CHANNEL_ID = "notificationChannelId"
        const val NOTIFICATION_CHANNEL_NAME = "notificationChannelName"
        const val TIMER_INTERVAL = 1000L
        val START_SERVICE = "start service"
        val RESUME_SERVICE = "resume service"
        val STOP_SERVICE = "stop service"
    }
}