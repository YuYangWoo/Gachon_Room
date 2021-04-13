package com.cookandroid.gachon_study_room.ui.main.view.dialog

import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.util.Log
import android.widget.NumberPicker
import android.widget.TimePicker


class CustomTimePickerDialog(context: Context?, private val mTimeSetListener: OnTimeSetListener?,
                             hourOfDay: Int, minute: Int, is24HourView: Boolean) : TimePickerDialog(context, null, hourOfDay,
        minute / TIME_PICKER_INTERVAL, is24HourView) {
    val timePicker by lazy { findViewById<TimePicker>(Resources.getSystem().getIdentifier(
            "timePicker",
            "id",
            "android"
    ))
    }
    override fun updateTime(hourOfDay: Int, minuteOfHour: Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            this.timePicker!!.hour = hourOfDay
            this.timePicker!!.minute = minuteOfHour / TIME_PICKER_INTERVAL
        }
        else {
            this.timePicker!!.currentHour = hourOfDay
            this.timePicker!!.currentMinute = minuteOfHour / TIME_PICKER_INTERVAL
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        when (which) {
            BUTTON_POSITIVE -> {
                Log.d("test", "확인버튼 누름.")
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    mTimeSetListener?.onTimeSet(this.timePicker, this.timePicker!!.hour, this.timePicker!!.minute * TIME_PICKER_INTERVAL)
                } else {
                    mTimeSetListener?.onTimeSet(this.timePicker, this.timePicker!!.currentHour, this.timePicker!!.currentMinute * TIME_PICKER_INTERVAL)
                }
            }
            BUTTON_NEGATIVE -> cancel()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        try {
            // 분관리
            val minutePicker = this.timePicker.findViewById<NumberPicker>(Resources.getSystem().getIdentifier(
                    "minute",
                    "id",
                    "android"
            ))

            val hourPicker = this.timePicker.findViewById<NumberPicker>(Resources.getSystem().getIdentifier(
                    "hour",
                    "id",
                    "android"
            ))
            minutePicker.minValue = 0
            minutePicker.maxValue = 60 / TIME_PICKER_INTERVAL -1
            val displayedValues: MutableList<String> = ArrayList()
            var i = 0
            while (i < 60) {
                displayedValues.add(String.format("%02d", i))
                i += TIME_PICKER_INTERVAL
            }
            minutePicker.displayedValues = displayedValues.toTypedArray()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("test", e.toString())
        }
    }

    companion object {
        private const val TIME_PICKER_INTERVAL = 10
    }
}