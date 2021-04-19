package com.cookandroid.gachon_study_room.ui.main.view.fragment

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.ReservationStatus2Binding
import com.cookandroid.gachon_study_room.ui.adapter.SeatStatus2Adapter
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import java.util.*

class StatusFragment : BaseFragment<ReservationStatus2Binding>(R.layout.reservation_status2) {
    private var color = ArrayList<Int>()

    override fun init() {
        super.init()
        statusDataSet()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        with(binding.recyclerStatus) {
            adapter = SeatStatus2Adapter(requireContext()).apply {
                data = color
                notifyDataSetChanged()
            }
            layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun statusDataSet() {

        for(i in 0 until 90) { // 90개 status_count
            if(i < ReservationFragment.status_count) {
                color.add(0)
            }
            else {
                color.add(1)
            }
        }
        color.add(0,-1)
    }

}