package com.example.myapplication.ui.main.drivingroute

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
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

class DrivingRouteFragment : Fragment(), OnMapReadyCallback {

    private val drivingRouteViewModel: DrivingRouteViewModel by viewModel()
    var map: GoogleMap? = null
    val args: DrivingRouteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("map : ", "onCreateView")
        return inflater.inflate(R.layout.fragment_driving_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("map : ", "onViewCreated")
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        val startTime = args.startTime
        drivingRouteViewModel.getDrivingRoute(startTime)
    }

    /*
    * 지도에 경로와 출발/도착지점을 지도 가운데 표시
    * */
    private fun drawOnMap(list: List<LatLng>) {
        Log.d("map : ", "drawOnMap")
        if (list.isNotEmpty()) {
            val departure = list[0]
            val destination = list[list.size - 1]

            val e = list.maxByOrNull { it.longitude }?.longitude
            val w = list.minByOrNull { it.longitude }?.longitude
            val s = list.minByOrNull { it.latitude }?.latitude
            val n = list.maxByOrNull { it.latitude }?.latitude

            map?.apply {
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
                moveCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        LatLngBounds(LatLng(s!!, w!!), LatLng(n!!, e!!)),
                        400,
                        400,
                        10
                    )
                )
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        Log.d("map : ", "callback")
        drivingRouteViewModel.drivingRoute.observe(viewLifecycleOwner, { list ->
            list?.let { drawOnMap(it) }
        })
    }
}