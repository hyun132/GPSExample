package com.example.myapplication.ui.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.services.TrackingService
import com.example.myapplication.services.TrackingService.Companion.START_SERVICE
import com.example.myapplication.services.TrackingService.Companion.STOP_SERVICE
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModel()
    override var layoutResourceId: Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            fragment = this@HomeFragment
        }
    }

    fun startService(view: View, isServiceRunning: LiveData<Boolean>) {
        println("button clicked")
        val action = if (isServiceRunning.value == true) STOP_SERVICE else START_SERVICE

        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }

    fun goToDrivingList(view: View) {
        findNavController().navigate(R.id.action_homeFragment_to_drivingListFragment)
    }
}