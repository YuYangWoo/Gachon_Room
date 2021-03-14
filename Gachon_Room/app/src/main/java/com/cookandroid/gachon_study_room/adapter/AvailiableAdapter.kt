package com.cookandroid.gachon_study_room.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.gachon_study_room.data.room.Availiable
import com.cookandroid.gachon_study_room.databinding.HolderAvailiableBinding

class AvailiableAdapter : RecyclerView.Adapter<AvailiableAdapter.ListViewHolder>() {
    var data =  ArrayList<Availiable>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = HolderAvailiableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
       return data.size
    }

    class ListViewHolder(private val binding: HolderAvailiableBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Availiable) {
            binding.use = data
        }
    }

}