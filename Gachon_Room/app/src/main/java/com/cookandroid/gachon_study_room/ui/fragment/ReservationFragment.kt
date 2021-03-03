package com.cookandroid.gachon_study_room.ui.fragment

import android.util.Log
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.RoomData
import com.cookandroid.gachon_study_room.databinding.FragmentReservationBinding
import com.cookandroid.gachon_study_room.singleton.RoomRequest
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import java.io.Serializable
import java.util.*

class ReservationFragment : BaseFragment<FragmentReservationBinding>(R.layout.fragment_reservation) {
    private val args: ReservationFragmentArgs by navArgs()
    override fun init() {
        super.init()

        var room = args.room
        seatView(room)
        Log.d("test", room.room.seat.size.toString()) // 열
        Log.d("test", room.room.seat[0].size.toString()) //행
    }

    private fun seatView(room: RoomData) {
        var garo = room.room.seat[0].size.toString()
        var sero = room.room.seat.size.toString()


    }
}