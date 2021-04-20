package com.cookandroid.gachon_study_room.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.gachon_study_room.data.model.SeatStatus
import com.cookandroid.gachon_study_room.databinding.HolderStatus2TimeBinding

class SeatStatus2TimeAdapter : RecyclerView.Adapter<SeatStatus2TimeAdapter.TimeViewHolder>() {
    var data = ArrayList<SeatStatus>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SeatStatus2TimeAdapter.TimeViewHolder {
        val binding = HolderStatus2TimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeatStatus2TimeAdapter.TimeViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class TimeViewHolder(private val binding: HolderStatus2TimeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(seat: SeatStatus) {
            if(seat.time != 0) {
                binding.timeBar.text = seat.time.toString()
            }
        }
    }
}