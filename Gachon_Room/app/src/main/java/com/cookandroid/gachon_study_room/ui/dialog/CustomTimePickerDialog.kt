package com.cookandroid.gachon_study_room.ui.dialog

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker

class CustomTimePickerDialog(context: Context?, item:Int, callBack: OnTimeSetListener, hourOfDay: Int, minute: Int, is24HourView: Boolean) :  TimePickerDialog(context, item, callBack, hourOfDay, minute, is24HourView) {
    private var mIgnoreEvent = false

    /*
 * (non-Javadoc)
 * @see android.app.TimePickerDialog#onTimeChanged(android.widget.TimePicker, int, int)
 * Implements Time Change Interval
 */

    override fun onTimeChanged(
            timePicker: TimePicker,
            hourOfDay: Int,
            minute: Int
    ) {
        var minute = minute
        super.onTimeChanged(timePicker, hourOfDay, minute)
        this.setTitle("예약 시간 선택")
        if (!mIgnoreEvent) {
            minute = getRoundedMinute(minute)
            mIgnoreEvent = true
            timePicker.currentMinute = minute
            mIgnoreEvent = false
        }
    }

    companion object {
        const val TIME_PICKER_INTERVAL = 10
        fun getRoundedMinute(minute: Int): Int {
            var minute = minute
            if (minute % TIME_PICKER_INTERVAL != 0) {
                val minuteFloor = minute - minute % TIME_PICKER_INTERVAL
                minute = minuteFloor + if (minute == minuteFloor + 1) TIME_PICKER_INTERVAL else 0
                if (minute == 60) minute = 0
            }
            return minute
        }
    }
}