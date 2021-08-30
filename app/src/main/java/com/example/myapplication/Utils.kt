package com.example.myapplication

import android.location.Location
import java.util.*

fun calculationTimeDiff(date1: Date, date2: Date): CharSequence {
    val diff: Long = date1.getTime() - date2.getTime()
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    return "${hours}시간 ${minutes - 60 * hours}분 소요"
}

fun calculateDistance(loc1: Location, loc2: Location): Float {
    val distance = FloatArray(1)
    Location.distanceBetween(
        loc1.latitude,
        loc1.longitude,
        loc2.latitude,
        loc2.longitude,
        distance
    )
    return distance[0]
}