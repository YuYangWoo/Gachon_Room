package com.cookandroid.gachon_study_room.ui.fragment.seat

import android.app.Dialog
import android.os.Bundle
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

class RoomListFragment : BaseBottomSheet<FragmentRoomListBinding>(R.layout.fragment_room_list) {
//    private val args: ReservationFragmentArgs by navArgs()

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
}