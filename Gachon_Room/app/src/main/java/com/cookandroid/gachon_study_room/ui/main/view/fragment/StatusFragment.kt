package com.cookandroid.gachon_study_room.ui.main.view.fragment

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.SeatStatus
import com.cookandroid.gachon_study_room.data.singleton.TimeRequest
import com.cookandroid.gachon_study_room.databinding.ReservationStatus2Binding
import com.cookandroid.gachon_study_room.ui.adapter.SeatStatus2Adapter
import com.cookandroid.gachon_study_room.ui.adapter.SeatStatus2TimeAdapter
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import java.util.*

class StatusFragment : BaseFragment<ReservationStatus2Binding>(R.layout.reservation_status2) {
    private var seatStatus = ArrayList<SeatStatus>()
    var now = TimeRequest.statusTodayTime()
    var pos = 0
    override fun init() {
        super.init()
        statusDataSet()
        setRecyclerView()
        setRecyclerTime()
    }

    private fun setRecyclerView() {
        with(binding.recyclerStatus) {
            adapter = SeatStatus2Adapter(requireContext()).apply {
                data = seatStatus
//                stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                notifyDataSetChanged()
            }
            layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setRecyclerTime() {
        with(binding.recyclerTime) {
            adapter = SeatStatus2TimeAdapter().apply {
                data = seatStatus
                notifyDataSetChanged()
            }
            layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun statusDataSet() {
        for(i in 0 until 144) { // 90개 status_count
            if(i % 6 == 0) {
                seatStatus.add(SeatStatus(-1, now, pos++))
                now += 1
            }
            else  {
                seatStatus.add(SeatStatus(1, 0, pos++))
            }

        }
    }

}