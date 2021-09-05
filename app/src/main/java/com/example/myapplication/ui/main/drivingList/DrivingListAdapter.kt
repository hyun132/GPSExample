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
    interface DrivingItemClickListener {
        fun onClick(startTime: Date)
    }

    private var itemClickListener: DrivingItemClickListener? = null

    fun setOnDrivingItemClickListener(listener: DrivingItemClickListener?) {
        if (listener != null) {
            this.itemClickListener = listener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrivingListViewHolder {
        val binding =
            DrivingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DrivingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DrivingListViewHolder, position: Int) {
        val visibility =
            position == 0 || (currentList[position].trackingEndTime.date != currentList[position - 1].trackingEndTime.date)
        holder.bind(currentList[position], visibility, itemClickListener)
    }

    class DrivingListViewHolder(
        private val binding: DrivingListItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            trackingLog: TrackingLog,
            visibility: Boolean,
            listener: DrivingItemClickListener?
        ) {
            binding.apply {
                this.trackingLog = trackingLog
                dateVisibility = visibility
                root.setOnClickListener {
                    listener?.onClick(trackingLog.trackingStartTime)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TrackingLog>() {
        override fun areItemsTheSame(oldItem: TrackingLog, newItem: TrackingLog): Boolean {
            return oldItem.trackingStartTime == newItem.trackingStartTime
        }

        override fun areContentsTheSame(oldItem: TrackingLog, newItem: TrackingLog): Boolean {
            return oldItem == newItem
        }
    }
}