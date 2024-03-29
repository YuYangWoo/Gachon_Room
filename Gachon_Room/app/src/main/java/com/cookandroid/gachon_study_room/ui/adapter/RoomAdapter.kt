package com.cookandroid.gachon_study_room.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.room.Room
import com.cookandroid.gachon_study_room.data.model.room.RoomsData
import com.cookandroid.gachon_study_room.databinding.HolderRoomListBinding
import com.cookandroid.gachon_study_room.ui.main.view.fragment.RoomListFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RoomAdapter() : RecyclerView.Adapter<RoomAdapter.ListViewHolder>() {
    var data = ArrayList<Room>()
    var rooms = RoomsData()
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = HolderRoomListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding, rooms, context)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind(data[position].roomName)
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
                context.supportFragmentManager.findFragmentByTag("Modal").let { (it as BottomSheetDialogFragment).dismiss() }
            }
        }
    }
}