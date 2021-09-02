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
import com.google.android.gms.maps.model.LatLngBounds
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

        var w: Double = destination.latitude
        var e: Double = departure.latitude
        var n: Double = departure.longitude
        var s: Double = destination.longitude

        if (departure.latitude < destination.latitude) { //가로
            n = destination.latitude
            s = departure.latitude
        }
        if (departure.longitude < destination.longitude) { // 세로
            e = destination.longitude
            w = departure.longitude
        }

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

            uiSettings.apply {
                isZoomControlsEnabled = true
                isMyLocationButtonEnabled = true
            }
            moveCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds(LatLng(s, w), LatLng(n, e)),400,400,10))
        }

    }
}