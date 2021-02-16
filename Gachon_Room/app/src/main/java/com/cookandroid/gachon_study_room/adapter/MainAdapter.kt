package com.cookandroid.gachon_study_room.adapter

import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.HolderSeatListBinding

class MainAdapter : RecyclerView.Adapter<MainAdapter.ListViewHolder>() {
    var data = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = HolderSeatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ListViewHolder(private var binding: HolderSeatListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: String) {
            binding.list = data
            Log.d("test", data)
        }
        init {
          binding.root.setOnClickListener {
              Log.d("test", binding.list.toString())
          }
        }
    }
}