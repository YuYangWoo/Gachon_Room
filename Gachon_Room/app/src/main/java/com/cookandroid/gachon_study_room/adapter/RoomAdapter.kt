package com.cookandroid.gachon_study_room.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.Room
import com.cookandroid.gachon_study_room.data.RoomsData
import com.cookandroid.gachon_study_room.databinding.HolderMainListBinding
import com.cookandroid.gachon_study_room.databinding.HolderRoomListBinding
import com.cookandroid.gachon_study_room.singleton.RoomRequest
import com.cookandroid.gachon_study_room.singleton.RoomsRequest
import com.cookandroid.gachon_study_room.ui.fragment.MainFragmentDirections
import com.cookandroid.gachon_study_room.ui.fragment.seat.RoomListFragmentDirections

class RoomAdapter : RecyclerView.Adapter<RoomAdapter.ListViewHolder>() {
    var data = ArrayList<Room>()
    var rooms = RoomsData()
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = HolderRoomListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding, rooms, context)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind(data[position].name)
    }

    override fun getItemCount(): Int {
        return data.size
    }
    class ListViewHolder(private var binding: HolderRoomListBinding, rooms: RoomsData, context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: String) {
            binding.list = data
        }
        init {
            binding.root.setOnClickListener {

                val navHostFragment = (context as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(RoomListFragmentDirections.actionGlobalReservationFragment(rooms, binding.list.toString()))
            }
        }
    }
}