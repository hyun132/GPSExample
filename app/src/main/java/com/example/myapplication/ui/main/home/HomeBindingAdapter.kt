package com.example.myapplication.ui.main.home

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.getTakenTime
import com.example.myapplication.model.TrackingLog
import com.example.myapplication.ui.main.drivingList.DrivingListAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object HomeBindingAdapter {

    @JvmStatic
    @BindingAdapter("day")
    fun getDay(view: TextView, date: Long) {
        val formatter: DateFormat = SimpleDateFormat("yyyy/MM/dd",Locale.KOREA)
        view.text = formatter.format(date)
    }

    @JvmStatic
    @BindingAdapter("time")
    fun getTime(view: TextView, date: Long) {
        val formatter: DateFormat = SimpleDateFormat("HH:mm:ss",Locale.KOREA)
        view.text = formatter.format(date)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["start", "end"])
    fun getDrivingTime(view: TextView, start: Long, end: Long) {
        val drivingTime = start.getTakenTime(end)
        view.text = drivingTime + " 소요"
    }
}