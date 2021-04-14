package com.cookandroid.gachon_study_room.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.room.Room
import com.cookandroid.gachon_study_room.databinding.HolderStatusBinding
import com.cookandroid.gachon_study_room.databinding.ReservationStatusBinding

class SeatStatusAdapter(private var context: Context) : RecyclerView.Adapter<SeatStatusAdapter.StatusViewHolder>() {
    var data = ArrayList<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding = HolderStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
//        Log.d("test", position.toString())
     holder.onBind(data[position], position)
    }

    override fun getItemCount(): Int {
        Log.d("test", data.size.toString())
        return data.size
    }

    inner class StatusViewHolder(private val binding: HolderStatusBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(img: Int, pos: Int) {
            if(img == 0) {
                binding.seatStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.gray))
            }
            else {
                binding.seatStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.mainColor))
            }
            if((pos % 18) == 0) {
                var params = binding.txtTime.layoutParams
                params.width = 10
                binding.txtTime.layoutParams = params
                binding.txtTime.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }

}