package com.cookandroid.gachon_study_room.ui.fragment.seat

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.adapter.RoomAdapter
import com.cookandroid.gachon_study_room.data.room.Room
import com.cookandroid.gachon_study_room.data.room.RoomsData
import com.cookandroid.gachon_study_room.databinding.FragmentRoomListBinding
import com.cookandroid.gachon_study_room.singleton.RoomsRequest
import com.cookandroid.gachon_study_room.ui.base.BaseBottomSheet

class RoomListFragment constructor() : BaseBottomSheet<FragmentRoomListBinding>(R.layout.fragment_room_list) {
    private lateinit var conte: Context

    constructor(context: Context) : this() {
        this.conte = context
    }

    override fun init() {
        super.init()
        var rooms = RoomsRequest.room
        setRecyclerView(rooms.rooms, rooms)
        cancel()
        binding.room = this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    private fun setRecyclerView(list: ArrayList<Room>, roomsData: RoomsData) {
        with(binding.recyclerList) {
            adapter = RoomAdapter().apply {
                data = list
                rooms = roomsData
                context = requireContext()
                notifyDataSetChanged()
            }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

   private fun cancel() {
       binding.cancel.setOnClickListener {
           dismiss()
       }
    }

}