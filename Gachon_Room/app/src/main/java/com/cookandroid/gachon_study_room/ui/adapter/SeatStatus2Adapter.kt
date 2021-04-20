package com.cookandroid.gachon_study_room.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.HolderStatus2Binding
import com.cookandroid.gachon_study_room.databinding.HolderStatusBinding

class SeatStatus2Adapter(private var context: Context) :
        RecyclerView.Adapter<SeatStatus2Adapter.StatusViewHolder>() {
    var data = ArrayList<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding =
                HolderStatus2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.onBind(data[position], position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class StatusViewHolder(private val binding: HolderStatus2Binding) :
            RecyclerView.ViewHolder(binding.root) {
                var params = binding.timeBar.layoutParams
        var marginParams: ViewGroup.MarginLayoutParams = binding.seatStatus.layoutParams as ViewGroup.MarginLayoutParams
        fun onBind(img: Int, pos: Int) {
            if ((pos % 6) == 0) {
                // 검정바 생성
//                params.width = 5
//                binding.timeBar.layoutParams = params

                // 버튼 사이 margin주기
                marginParams.marginStart = 7
                marginParams.marginEnd = 7
                binding.seatStatus.layoutParams = marginParams
            }
            Log.d("test", pos.toString())
            when (img) {
                // 사용할 수 없으면
                0 -> {
                    binding.seatStatus.background = ContextCompat.getDrawable(context, R.drawable.status_list_edge)

                }
                // 사용할 수 있으면
                1 -> {
                    binding.seatStatus.background = ContextCompat.getDrawable(context, R.drawable.status_list_edge_available)
                }
                // 둘다 아니면


            }

        }
    }

}