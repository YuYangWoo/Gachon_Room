package com.cookandroid.gachon_study_room.ui.fragment.seat

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.adapter.MainAdapter
import com.cookandroid.gachon_study_room.adapter.RoomAdapter
import com.cookandroid.gachon_study_room.data.Room
import com.cookandroid.gachon_study_room.data.RoomsData
import com.cookandroid.gachon_study_room.databinding.FragmentRoomListBinding
import com.cookandroid.gachon_study_room.singleton.RoomsRequest
import com.cookandroid.gachon_study_room.ui.base.BaseBottomSheet
import com.cookandroid.gachon_study_room.ui.fragment.ReservationFragmentArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RoomListFragment constructor() : BaseBottomSheet<FragmentRoomListBinding>(R.layout.fragment_room_list) {
//    private val args: ReservationFragmentArgs by navArgs()

    private lateinit var conte: Context

    constructor(context: Context) : this() {
        this.conte = context
    }

    override fun init() {
        super.init()
//        var rooms = args.rooms
        var rooms = RoomsRequest.room
        setRecyclerView(rooms.rooms, rooms)
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

    override fun onStop() {
        super.onStop()
        Log.d("test", "onstop")
        dismiss()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
        Log.d("test", "onpause")

    }
}