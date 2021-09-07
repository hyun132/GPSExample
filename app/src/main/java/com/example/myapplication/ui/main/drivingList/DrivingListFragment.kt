package com.example.myapplication.ui.main.drivingList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDrivingListBinding
import com.example.myapplication.ui.base.BaseFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

/* 주행목록 화면 */
class DrivingListFragment : BaseFragment<FragmentDrivingListBinding, DrivingListViewModel>() {

    override val viewModel: DrivingListViewModel by inject()
    override var layoutResourceId: Int = R.layout.fragment_driving_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drivingAdapter = DrivingListAdapter()
        setItemClickListener(drivingAdapter)

        binding.rvDrivingList.apply {
            adapter = drivingAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.drivingList.observe(viewLifecycleOwner, {
            drivingAdapter.submitList(it)
            viewModel.getTotalDrivingDistance()
        })
    }

    private fun setItemClickListener(drivingAdapter: DrivingListAdapter) {
        drivingAdapter.setOnDrivingItemClickListener(object :
            DrivingListAdapter.DrivingItemClickListener {
            override fun onClick(startTime: Long) {
                val action =
                    DrivingListFragmentDirections.actionDrivingListFragmentToDrivingRouteFragment(
                        startTime
                    )
                findNavController().navigate(action)
            }
        }
        )
    }
}