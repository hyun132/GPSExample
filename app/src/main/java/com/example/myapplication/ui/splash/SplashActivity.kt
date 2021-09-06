package com.example.myapplication.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.ui.login.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * splash 시작 시 권한체크를 하고 권한 없으면 요청/종료
 * 권한 있을 시 LoginActivity로 이동
 *
 * android 12부터 커스텀스플래시 안되나봄..
 * 이 부분 변경필요
 */
class SplashActivity : AppCompatActivity() {

    val TAG = "SplashActivity"

    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private val resultLauner =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                checkPermissions()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(1000)
            checkPermissions()
        }
    }

    private fun checkPermissions() {
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
                    goToMainActivity()
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
                    goToMainActivity()
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
            goToMainActivity()
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
                    Toast.makeText(this, "권한 설정됨", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    // Permission request was denied.
                    Toast.makeText(this, "서비스 이용을 위해 권한을 설정해주세요.", Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch {
                        delay(1000)
                        openPermissionSetting() // startActivity*** 대신 launcher사용..
                    }
                }
            }
        }
    }

    /* 권한 거절된 경우 앱 설정 화면으로 이동한다 */
    private fun openPermissionSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            data = Uri.parse("package:$packageName")
        }
        resultLauner.launch(intent)
    }

    private fun goToMainActivity() {
        Toast.makeText(this, "권한 설정됨", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    // 상수부분은 추후 const파일에 저장할 것
    companion object {
        const val LOCATION_PERMISSION_CODE = 1001
        const val BACKGROUND_LOCATION_PERMISSION_CODE = 1002
    }
}