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

class ReservationFragment : BaseFragment<FragmentReservationBinding>(R.layout.fragment_reservation) {
    private lateinit var room:RoomData
    private val args: ReservationFragmentArgs by navArgs()
    override fun init() {
        super.init()

        room = args.room
        Log.d("test", room.message)
    }

}