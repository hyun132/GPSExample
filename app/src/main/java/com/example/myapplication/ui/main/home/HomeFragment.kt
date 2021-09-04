package com.example.myapplication.ui.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.services.TrackingService
import com.example.myapplication.services.TrackingService.Companion.START_SERVICE
import com.example.myapplication.services.TrackingService.Companion.STOP_SERVICE
import com.example.myapplication.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.apply {
            viewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
            fragment = this@HomeFragment
        }
        return binding.root
    }

    fun startService(view: View,isServiceRunning:LiveData<Boolean>) {
        println("button clicked")
        val action = if(isServiceRunning.value == true) STOP_SERVICE else START_SERVICE

        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }

    fun goToDrivingList(view: View) {
        findNavController().navigate(R.id.action_homeFragment_to_drivingListFragment)
    }

}