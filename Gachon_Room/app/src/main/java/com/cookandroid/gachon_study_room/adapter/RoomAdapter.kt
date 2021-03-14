package com.cookandroid.gachon_study_room.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.room.Room
import com.cookandroid.gachon_study_room.data.room.RoomsData
import com.cookandroid.gachon_study_room.databinding.HolderRoomListBinding
import com.cookandroid.gachon_study_room.ui.dialog.ProgressDialog
import com.cookandroid.gachon_study_room.ui.fragment.seat.RoomListFragment
import com.cookandroid.gachon_study_room.ui.fragment.seat.RoomListFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RoomAdapter(dialog: ProgressDialog) : RecyclerView.Adapter<RoomAdapter.ListViewHolder>() {
    var data = ArrayList<Room>()
    var rooms = RoomsData()
    var dialog = dialog
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = HolderRoomListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding, rooms, context, dialog)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind(data[position].name)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ListViewHolder(private var binding: HolderRoomListBinding, rooms: RoomsData, context: Context, dialog: ProgressDialog) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: String) {
            binding.list = data
        }

        init {
            binding.root.setOnClickListener {
             dialog.apply{
                    window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    show()
                }
                val navHostFragment = (context as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(RoomListFragmentDirections.actionGlobalReservationFragment(rooms, binding.list.toString()))
                context.supportFragmentManager.findFragmentByTag("Modal").let { (it as BottomSheetDialogFragment).dismiss() }
            }
        }
    }
}