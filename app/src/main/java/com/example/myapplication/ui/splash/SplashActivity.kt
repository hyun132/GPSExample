package com.example.myapplication.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.ActivitySplashBinding
import com.example.myapplication.ui.login.LoginActivity
import com.example.myapplication.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar

/**
 * splash 시작 시 권한체크를 하고 권한 없으면 요청/종료
 * 권한 있을 시 LoginActivity로 이동
 *
 * android 12부터 커스텀스플래시 안되나봄..
 * 이 부분 변경필요
 */
class SplashActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)

    // 상수부분은 추후 const파일에 저장할 것
    companion object {
        const val LOCATION_PERMISSION_CODE = 1001
        const val BACKGROUND_LOCATION_PERMISSION_CODE = 1002
    }

    val TAG = "SplashActivity"

    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(mainLooper).postDelayed({
            checkPermissions()
        }, 1000)

    }

    val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    fun checkPermissions() {
        permissions.map { checkThisPermissionGranted(it) }
    }

    /**
     * 권한확인
     */
    private fun checkThisPermissionGranted(permission: String) {
        //when your app receives foreground location access, it automatically receives background location access as well.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) -> {
                    // You can use the API that requires the permission.
                    checkBackgroundPermissionGranted()
                }
                shouldShowRequestPermissionRationale(permission) -> {
                    Toast.makeText(this, "서비스 사용을 위해서 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                    ActivityCompat.requestPermissions(
                        this,
                        permissions,
                        LOCATION_PERMISSION_CODE
                    )
                }
                else -> {
                    Log.d(TAG, "in else")
                    ActivityCompat.requestPermissions(
                        this,
                        permissions,
                        LOCATION_PERMISSION_CODE
                    )
                }
            }
        }
    }

    private fun checkBackgroundPermissionGranted() {
        //when your app receives foreground location access, it automatically receives background location access as well.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            when {
                (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) -> {
                    // You can use the API that requires the permission.
                    Toast.makeText(this, "권한 설정됨", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
//                    startActivity(Intent(this, MainActivity::class.java))
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION) -> {
                    Log.d(TAG, "in background rationale")
                    Toast.makeText(this, "서비스 사용을 위해서 백그라운드 위치 권한이 필요합니다.", Toast.LENGTH_SHORT)
                        .show()
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        BACKGROUND_LOCATION_PERMISSION_CODE
                    )
                }
                else -> {
                    Log.d(TAG, "in else")
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        BACKGROUND_LOCATION_PERMISSION_CODE
                    )
                }
            }
        } else {
            Toast.makeText(this, "권한 설정됨", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, MainActivity::class.java))
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /* 권한 요청결과 */
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted. Start camera preview Activity.
                    checkBackgroundPermissionGranted()
                } else {
                    // Permission request was denied.
                    Toast.makeText(this, "서비스 이용을 위해 권한을 설정해주세요.", Toast.LENGTH_SHORT).show()
                    Handler(mainLooper).postDelayed({
                        finish()
                    }, 1000)
                }
            }
            BACKGROUND_LOCATION_PERMISSION_CODE -> {
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted. Start camera preview Activity.
                    Toast.makeText(this, "권한 설성 완료", Toast.LENGTH_SHORT).show()
                    /* 여기서 로그인 체크하고 액티비티 분기*/
//                    startActivity(Intent(this, MainActivity::class.java))
                    startActivity(Intent(this, LoginActivity::class.java))

                } else {
                    // Permission request was denied.
                    Toast.makeText(this, "서비스 이용을 위해 권한을 설정해주세요.", Toast.LENGTH_SHORT).show()
                    Handler(mainLooper).postDelayed({
                        finish()
                    }, 1000)
                }
            }
        }


    }
}