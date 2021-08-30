package com.example.myapplication.ui.main.home

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.TrackingService
import com.example.myapplication.TrackingService.Companion.START_SERVICE
import com.example.myapplication.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            //이 부분 추후 데이터바인딩으로 처리
            tvTrackingStartButton.setOnClickListener {
                println("button clicked")
                Intent(requireContext(), TrackingService::class.java).also {
                    it.action = START_SERVICE
                    requireContext().startService(it)
                }
            }

            tvGoToDrivingListButton.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_drivingListFragment)
            }
        }


//        homeViewModel.savedTrackingList.observe(viewLifecycleOwner,{
//            it.map { item -> println("loaded data from room! >> ${item.locationLog}") }
//        })

    }

}