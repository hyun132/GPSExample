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
    @BindingAdapter("app:buttonText")
    fun setButtonText(view: TextView, serviceIsActive: LiveData<Boolean>) {
        serviceIsActive.let {
            view.text = if (it.value == true) "STOP" else "START"
        }
    }

    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    @BindingAdapter("day")
    fun getDay(view: TextView, date: Date) {
        val formatter: DateFormat = SimpleDateFormat("yyyy/MM/dd")
        view.text = formatter.format(date.time)
    }

    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    @BindingAdapter("time")
    fun getTime(view: TextView, date: Date) {
        val formatter: DateFormat = SimpleDateFormat("HH:mm:ss")
        view.text = formatter.format(date.time)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["start", "end"])
    fun getDrivingTime(view: TextView, start: Date, end: Date) {
        val drivingTime = start.getTakenTime(end)
        val formatter: DateFormat = SimpleDateFormat("HH:mm:ss")
        view.text = drivingTime +" 소요"
    }

//    @JvmStatic
//    @BindingAdapter("trackingList")
//    fun bindTrackingList(recyclerView: RecyclerView,list: LiveData<List<TrackingLog>>) {
//        if (recyclerView.adapter == null) {
//            recyclerView.apply {
//                layoutManager = LinearLayoutManager(recyclerView.context)
//                adapter = DrivingListAdapter()
//            }
//        }
//        (recyclerView.adapter as DrivingListAdapter).submitList(list.value)
//    }
}