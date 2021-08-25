package com.example.myapplication.model

import com.google.gson.JsonObject

data class LoginResponse(
    val message : String,
    val code : String,
    val __go_checksum__ : Boolean,
    val data : JsonObject?//타입..?
)
