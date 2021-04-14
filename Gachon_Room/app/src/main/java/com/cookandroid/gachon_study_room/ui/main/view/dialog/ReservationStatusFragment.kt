package com.cookandroid.gachon_study_room.ui.main.view.dialog

import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.databinding.ReservationStatusBinding
import com.cookandroid.gachon_study_room.ui.adapter.SeatStatusAdapter
import com.cookandroid.gachon_study_room.ui.base.BaseBottomSheet
import com.cookandroid.gachon_study_room.ui.base.BaseDialog
import com.cookandroid.gachon_study_room.ui.base.BaseDialogFragment
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

class ReservationStatusFragment : BaseFragment<ReservationStatusBinding>(R.layout.reservation_status) {
    var color = ArrayList<Int>()
    override fun init() {
        super.init()
        txtSet()
        setRecyclerStatus()
    }

    private fun txtSet() {
        endCal.set(Calendar.MONTH, month)
        endCal.set(Calendar.DAY_OF_MONTH, day)
        binding.txtStartDate.text = timeFormat.format(cal.time) + "\n09:00"
        binding.txtEndDate.text = timeFormat.format(endCal.time) + "\n00:00"
        for(i in 0..90) {
            if(i <= count) {
                color.add(0)
            }
            else {
                color.add(1)
            }
        }
    }

    private fun setRecyclerStatus() {
        with(binding.recyclerStatus) {
            adapter = SeatStatusAdapter(context).apply {
                data = color
                notifyDataSetChanged()
            }
            layoutManager = object : GridLayoutManager(context, color.size) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }
            setHasFixedSize(true)
        }
    }

    companion object {
        val cal = Calendar.getInstance()
        val endCal = Calendar.getInstance()
        val month = endCal.get(Calendar.MONTH)
        val day = endCal.get(Calendar.DAY_OF_MONTH) + 1
        val hour = cal.get(Calendar.HOUR_OF_DAY) //16시
        val minute = cal.get(Calendar.MINUTE) // 49분
        val count = ((hour-9)*6) + ((minute/10)+1)
        val timeFormat = SimpleDateFormat("MM/dd")

    }
}