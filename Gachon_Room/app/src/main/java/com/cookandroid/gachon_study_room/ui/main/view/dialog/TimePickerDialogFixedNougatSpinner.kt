package com.cookandroid.gachon_study_room.ui.main.view.dialog

import android.R
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.content.res.TypedArray
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.widget.NumberPicker
import android.widget.TimePicker
import java.lang.reflect.Constructor
import java.lang.reflect.Field


class TimePickerDialogFixedNougatSpinner(context: Context?, private val mTimeSetListener: OnTimeSetListener?,
                                         hourOfDay: Int, minute: Int, is24HourView: Boolean) : TimePickerDialog(context, null, hourOfDay,
        minute / TIME_PICKER_INTERVAL, is24HourView) {

    init {
        fixSpinner(context!!, hourOfDay, minute, is24HourView)
    }


    private fun fixSpinner(context: Context, hourOfDay: Int, minute: Int, is24HourView: Boolean) { // fixes the bug in API 24
        try {
            // Get the theme's android:timePickerMode
            val MODE_SPINNER = 1
            val styleableClass = Class.forName("com.android.internal.R\$styleable")
            val timePickerStyleableField: Field = styleableClass.getField("TimePicker")
            val timePickerStyleable = timePickerStyleableField.get(null) as IntArray
            val a: TypedArray = context.obtainStyledAttributes(null, timePickerStyleable, R.attr.timePickerStyle, 0)
            val timePickerModeStyleableField: Field = styleableClass.getField("TimePicker_timePickerMode")
            val timePickerModeStyleable: Int = timePickerModeStyleableField.getInt(null)
            val mode = a.getInt(timePickerModeStyleable, MODE_SPINNER)
            a.recycle()
            if (mode == MODE_SPINNER) {
                val timePicker = findField(TimePickerDialog::class.java, TimePicker::class.java, "mTimePicker")!!.get(this) as TimePicker
                val delegateClass = Class.forName("android.widget.TimePicker\$TimePickerDelegate")
                val delegateField: Field? = findField(TimePicker::class.java, delegateClass, "mDelegate")
                var delegate: Any = delegateField!!.get(timePicker)
                val spinnerDelegateClass: Class<*> = Class.forName("android.widget.TimePickerSpinnerDelegate")
                // In 7.0 Nougat for some reason the timePickerMode is ignored and the delegate is TimePickerClockDelegate
                if (delegate.javaClass != spinnerDelegateClass) {
                    delegateField.set(timePicker, null) // throw out the TimePickerClockDelegate!
                    timePicker.removeAllViews() // remove the TimePickerClockDelegate views
                    val spinnerDelegateConstructor = spinnerDelegateClass.getConstructor(TimePicker::class.java, Context::class.java, AttributeSet::class.java, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                    spinnerDelegateConstructor.isAccessible = true
                    // Instantiate a TimePickerSpinnerDelegate
                    delegate = spinnerDelegateConstructor.newInstance(timePicker, context, null, R.attr.timePickerStyle, 0)
                    delegateField!!.set(timePicker, delegate) // set the TimePicker.mDelegate to the spinner delegate
                    // Set up the TimePicker again, with the TimePickerSpinnerDelegate
                    timePicker.setIs24HourView(is24HourView)
                    timePicker.currentHour = hourOfDay
                    timePicker.currentMinute = minute
                    timePicker.setOnTimeChangedListener(this)
                }
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }


    override fun updateTime(hourOfDay: Int, minuteOfHour: Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            this.timePicker!!.hour = hourOfDay
            this.timePicker!!.minute = minuteOfHour / TIME_PICKER_INTERVAL
        } else {
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


            minutePicker.minValue = 0
            minutePicker.maxValue = 60 / TIME_PICKER_INTERVAL - 1
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
        private fun findField(objectClass: Class<*>, fieldClass: Class<*>, expectedName: String): Field? {
            try {
                val field: Field = objectClass.getDeclaredField(expectedName)
                field.setAccessible(true)
                return field
            } catch (e: NoSuchFieldException) {
            } // ignore
            // search for it if it wasn't found under the expected ivar name
            for (searchField in objectClass.declaredFields) {
                if (searchField.type === fieldClass) {
                    searchField.isAccessible = true
                    return searchField
                }
            }
            return null
        }
    }

    val timePicker by lazy {
        findViewById<TimePicker>(Resources.getSystem().getIdentifier(
                "timePicker",
                "id",
                "android"
        ))
    }
}