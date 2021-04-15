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

class SeatStatusAdapter(private var context: Context) :
    RecyclerView.Adapter<SeatStatusAdapter.StatusViewHolder>() {
    var data = ArrayList<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding =
            HolderStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.onBind(data[position], position)
    }

    override fun getItemCount(): Int {
        Log.d("test", data.size.toString())
        return data.size
    }

    inner class StatusViewHolder(private val binding: HolderStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var params = binding.txtBar.layoutParams
        fun onBind(img: Int, pos: Int) {
            when (img) {
                // 사용할 수 없으면
                0 -> {
                    binding.seatStatus.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray
                        )
                    )
                }
                // 사용할 수 있으면
                1 -> {
                    binding.seatStatus.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.mainColor
                        )
                    )
                }
                // 둘다 아니면
                else -> {
                    params.width = 0
                    binding.txtBar.layoutParams = params
                }
            }
            if ((pos % 18) == 0 && pos != 0 && pos != 90) {
                params.width = 10
                binding.txtBar.layoutParams = params
                binding.txtBar.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }

}