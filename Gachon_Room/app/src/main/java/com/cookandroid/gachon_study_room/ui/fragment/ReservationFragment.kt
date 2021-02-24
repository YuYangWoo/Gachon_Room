package com.cookandroid.gachon_study_room.ui.fragment

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.FragmentReservationBinding
import com.cookandroid.gachon_study_room.singleton.RoomRequest
import com.cookandroid.gachon_study_room.ui.base.BaseFragment

class ReservationFragment : BaseFragment<FragmentReservationBinding>(R.layout.fragment_reservation) {
    private val url = "http://3.34.174.56:8080/room?college=TEST&room=Test"
    private val TAG = "MAIN"
    private lateinit var queue: RequestQueue

    override fun init() {
        super.init()
        queue = Volley.newRequestQueue(context)

        RoomRequest.request(requireContext(),url)
    }

}