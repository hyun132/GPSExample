package com.example.myapplication.ui.main.drivingList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.DrivingListItemBinding
import com.example.myapplication.getTakenTime
import com.example.myapplication.model.TrackingLog
import java.util.*

class DrivingListAdapter : ListAdapter<TrackingLog, DrivingListAdapter.DrivingListViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrivingListViewHolder {
        val binding =
            DrivingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DrivingListViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: DrivingListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    interface DrivingItemClickListener {
        fun onClick(startTime: Date)
    }

    private lateinit var itemClickListener: DrivingItemClickListener

    fun setOnDrivingItemClickListener(listener: DrivingItemClickListener) {
        this.itemClickListener = listener
    }

    class DrivingListViewHolder(
        private val binding: DrivingListItemBinding,
        private val listener: DrivingItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trackingLog: TrackingLog) {
            binding.apply {
                tvDay.text = trackingLog.trackingStartTime.toString()
                tvStartTime.text = trackingLog.trackingStartTime.toString()
                tvEndTime.text = trackingLog.trackingEndTime.toString()
                tvDrivingDistance.text = trackingLog.trackingDistance.toString()
                tvDrivingTime.text =
                    trackingLog.trackingStartTime.getTakenTime(trackingLog.trackingEndTime)
            }

            binding.root.setOnClickListener {
                listener.onClick(trackingLog.trackingStartTime)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TrackingLog>() {
        override fun areItemsTheSame(oldItem: TrackingLog, newItem: TrackingLog): Boolean {
            return oldItem.trackingId == newItem.trackingId
        }

        override fun areContentsTheSame(oldItem: TrackingLog, newItem: TrackingLog): Boolean {
            return oldItem == newItem
        }
    }
}