package com.example.myapplication

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.util.*

/*
* 소요시간 계산.
* */
fun Date.getTakenTime(endTime: Date): String {
    val diff = endTime.time - this.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    var result = ""

    if (hours > 0) result = "${hours}시간 ${minutes - 60 * hours}분"
    else result = "${minutes}분 ${seconds - 60 * minutes}초"

    return result
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

/*
* m단위 소수점 두자리인 km단위로 변경
* */
fun convertMeterToKm(distance: Float): String {
    return String.format(Locale.KOREA, "%.2fkm", distance / 1000)
}
