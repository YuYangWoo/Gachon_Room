package com.cookandroid.gachon_study_room.ui.fragment.seat

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.adapter.RoomAdapter
import com.cookandroid.gachon_study_room.data.room.Room
import com.cookandroid.gachon_study_room.data.room.RoomsData
import com.cookandroid.gachon_study_room.databinding.FragmentRoomListBinding
import com.cookandroid.gachon_study_room.singleton.RoomsRequest
import com.cookandroid.gachon_study_room.ui.base.BaseBottomSheet
import com.cookandroid.gachon_study_room.ui.dialog.ProgressDialog

class RoomListFragment constructor() : BaseBottomSheet<FragmentRoomListBinding>(R.layout.fragment_room_list) {
    private lateinit var conte: Context
    private lateinit var dialog: ProgressDialog
    constructor(context: Context) : this() {
        this.conte = context
    }

    override fun init() {
        super.init()
        var rooms = RoomsRequest.room
         dialog = ProgressDialog(requireContext())
        setRecyclerView(rooms.rooms, rooms, dialog)
        cancel()
        binding.room = this

//        (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    private fun setRecyclerView(list: ArrayList<Room>, roomsData: RoomsData, dialog: ProgressDialog) {
        with(binding.recyclerList) {
            adapter = RoomAdapter(dialog).apply {
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

    override fun onStop() {
        super.onStop()
        dialog.dismiss()
    }

}