package com.example.myapplication.ui.main.drivingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDrivingListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

/* 주행목록 화면 */
class DrivingListFragment : Fragment() {

    private val drivingListViewModel: DrivingListViewModel by viewModel()

    private lateinit var binding: FragmentDrivingListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDrivingListBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = drivingListViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drivingListViewModel.loadDrivingList()

        val drivingAdapter = DrivingListAdapter()
        drivingAdapter.setOnDrivingItemClickListener(object :
            DrivingListAdapter.DrivingItemClickListener {
            override fun onClick(startTime: Date) {
                val bundle = bundleOf("startTime" to startTime)
                findNavController().navigate(
                    R.id.action_drivingListFragment_to_drivingRouteFragment,
                    bundle
                )
            }
        }
        )

        binding.rvDrivingList.apply {
            adapter = drivingAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        drivingListViewModel.drivingList.observe(viewLifecycleOwner, {
            drivingAdapter.submitList(it)
        })
    }

    override fun onResume() {
        super.onResume()
        drivingListViewModel.loadDrivingList()
    }

    companion object {
    }
}