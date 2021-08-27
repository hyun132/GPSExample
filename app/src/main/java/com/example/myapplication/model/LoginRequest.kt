package com.example.myapplication.model

import java.io.Serializable

data class LoginRequest(
    val username: String,
    val password: String,
    val locale: String = "ko",
    val deviceId: String = "Android"
):Serializable
