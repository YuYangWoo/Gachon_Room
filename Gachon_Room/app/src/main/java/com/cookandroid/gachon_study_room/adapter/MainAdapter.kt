package com.cookandroid.gachon_study_room.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.HolderMainListBinding
import com.cookandroid.gachon_study_room.singleton.RoomRequest
import com.cookandroid.gachon_study_room.singleton.RoomsRequest
import com.cookandroid.gachon_study_room.ui.fragment.MainFragmentDirections

 class MainAdapter : RecyclerView.Adapter<MainAdapter.ListViewHolder>() {
    var data = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = HolderMainListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
  class ListViewHolder(private var binding: HolderMainListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: String) {
            binding.list = data
        }
        init {
            binding.root.setOnClickListener {
                when(binding.list) {
                    binding.root.resources.getString(R.string.confirm_my_seat) -> {
                        binding.root.findNavController().navigate(MainFragmentDirections.actionMainFragmentToMySeatDialog())
                    }
                    binding.root.resources.getString(R.string.choose_or_reservation) -> {
                        var room = RoomRequest.room
                        var rooms = RoomsRequest.room
                        binding.root.findNavController().navigate(MainFragmentDirections.actionMainFragmentToReservationFragment(room, rooms))
                    }
                    binding.root.resources.getString(R.string.confirm_seat) -> {
                        binding.root.findNavController().navigate(MainFragmentDirections.actionMainFragmentToQrCodeFragment())
                    }
                }
            }
        }
    }
}