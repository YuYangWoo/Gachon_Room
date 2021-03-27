package com.cookandroid.gachon_study_room.ui.fragment.seat

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.adapter.RoomAdapter
import com.cookandroid.gachon_study_room.data.room.Room
import com.cookandroid.gachon_study_room.data.room.RoomsData
import com.cookandroid.gachon_study_room.databinding.FragmentRoomListBinding
import com.cookandroid.gachon_study_room.service.RetrofitBuilder
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.base.BaseBottomSheet
import com.cookandroid.gachon_study_room.ui.dialog.ProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomListFragment : BaseBottomSheet<FragmentRoomListBinding>(R.layout.fragment_room_list) {
    private lateinit var dialog: ProgressDialog

    override fun init() {
        super.init()
        dialog = ProgressDialog(requireContext())
        setRecyclerView(dialog)
        cancel()
        binding.room = this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    private fun setRecyclerView(dialog: ProgressDialog) {
        var input = HashMap<String, String>()
//        input["college"] = MySharedPreferences.getInformation(requireContext()).college
        input["college"] = "TEST"
        RetrofitBuilder.api.roomsRequest(input).enqueue(object : Callback<RoomsData> {
            override fun onResponse(call: Call<RoomsData>, response: Response<RoomsData>) {
                if (response.isSuccessful) {
                    Log.d("test", "연결성공")
                    var roomsData = response.body()!!
                    roomsData.rooms[0].reserved

                    with(binding.recyclerList) {
                        adapter = RoomAdapter(dialog).apply {
                            data = roomsData.rooms
                            rooms = roomsData
                            context = requireContext()
                            notifyDataSetChanged()
                        }
                        layoutManager = LinearLayoutManager(requireContext())
                        setHasFixedSize(true)
                    }
                }
            }

            override fun onFailure(call: Call<RoomsData>, t: Throwable) {
                Log.d("test", "연결실패")
            }

        })


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