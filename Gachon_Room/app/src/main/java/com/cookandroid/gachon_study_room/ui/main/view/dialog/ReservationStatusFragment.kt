package com.cookandroid.gachon_study_room.ui.main.view.dialog

import android.content.Context
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.ReservationStatusBinding
import com.cookandroid.gachon_study_room.ui.adapter.SeatStatusAdapter
import com.cookandroid.gachon_study_room.ui.base.BaseBottomSheet
import com.cookandroid.gachon_study_room.ui.base.BaseDialog
import com.cookandroid.gachon_study_room.ui.base.BaseDialogFragment
import com.cookandroid.gachon_study_room.ui.base.BaseFragment

class ReservationStatusFragment : BaseFragment<ReservationStatusBinding>(R.layout.reservation_status) {
    var color = arrayListOf<Int>(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1)
    override fun init() {
        super.init()
        setRecyclerStatus()
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
}