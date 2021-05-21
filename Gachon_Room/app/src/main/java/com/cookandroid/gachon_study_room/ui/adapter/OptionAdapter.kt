package com.cookandroid.gachon_study_room.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.gachon_study_room.data.model.Option
import com.cookandroid.gachon_study_room.databinding.HolderOptionListBinding

class OptionAdapter : RecyclerView.Adapter<OptionAdapter.ListViewHolder>() {
    var data: ArrayList<Option> = arrayListOf(Option("", "만든이"), Option("", "단과대 오픈채팅"), Option("", "개발자 건의사항"), Option("", "오픈소스 라이브러리"), Option("","제휴문의"), Option("", "앱 버전 정보"))
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionAdapter.ListViewHolder {
        val binding = HolderOptionListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionAdapter.ListViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
     return data.size
    }

    inner class ListViewHolder(private val binding: HolderOptionListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(d: Option) {
            binding.list = d
        }
    }
}