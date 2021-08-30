package com.example.myapplication.ui.main.drivingroute

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class DrivingRouteFragment : Fragment() {

    private val drivingRouteViewModel: DrivingRouteViewModel by viewModel()
    lateinit var map: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val sydney = LatLng(-34.0, 151.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_driving_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        drivingRouteViewModel.startTime = arguments?.getSerializable("startTime") as Date
        drivingRouteViewModel.getDrivingRoute(drivingRouteViewModel.startTime)
        Log.d("test : ", drivingRouteViewModel.startTime.toString())

        drivingRouteViewModel.locationList.observe(viewLifecycleOwner, {
            drawOnMap(it)
        })
    }

    /*
    * 지도에 경로와 출발/도착지점 표시
    * */
    private fun drawOnMap(list: List<LatLng>) {
        val departure = list[0]
        val destination = list[list.size - 1]

//        var n: Double = destination.latitude
//        var s: Double = departure.latitude
//        var w: Double = departure.longitude
//        var e: Double = destination.longitude
//
//        if (departure.latitude > destination.latitude) {
//            n = departure.latitude
//            s = destination.latitude
//        }
//        if (departure.longitude < destination.longitude) {
//            w = destination.longitude
//            e = departure.longitude
//        }

//        println("top is : $n ,bottom is : $s ,west is : $w ,east is : $s center is ${LatLngBounds(LatLng(s,w), LatLng(n,e)).center}")

        map.apply {
            addPolyline(
                PolylineOptions().clickable(false)
                    .addAll(list)
                    .width(25f)
                    .color(Color.BLUE)
                    .geodesic(true)
            )
            addMarker(MarkerOptions().position(departure).title("출발"))
            addMarker(MarkerOptions().position(destination).title("도착"))
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    departure,
                    10F
                )
            )
            uiSettings.apply {
                isZoomControlsEnabled = true
                isMyLocationButtonEnabled = true
            }
//            setLatLngBoundsForCameraTarget(LatLngBounds(LatLng(s,w),LatLng(n,e)))
        }
    }
}