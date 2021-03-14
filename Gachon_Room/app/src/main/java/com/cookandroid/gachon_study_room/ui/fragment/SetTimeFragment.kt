package com.cookandroid.gachon_study_room.ui.fragment

import android.app.TimePickerDialog
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.FragmentSetTimeBinding
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import com.cookandroid.gachon_study_room.ui.dialog.CustomTimePickerDialog
import java.util.*

class SetTimeFragment : BaseFragment<FragmentSetTimeBinding>(R.layout.fragment_set_time) {
    override fun init() {
        super.init()
        binding.timer.setOnClickListener {
            val cal = Calendar.getInstance()


            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
            }


            val timePickerDialog: TimePickerDialog
            var hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
            val minute = CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance()[Calendar.MINUTE] + CustomTimePickerDialog.TIME_PICKER_INTERVAL)

            if (minute == 0) {
                hour += 1
            }

            //Theme_Holo_Light_Dialog_NoActionBar-> Timepicker 를 Spinner 형태로 전환
            timePickerDialog = CustomTimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeSetListener, hour, minute, true)
            timePickerDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent) //무늬 이쁘게 해주는거
            timePickerDialog.setTitle("예약 시간 선택")
            timePickerDialog.show()
        }
    }
}
