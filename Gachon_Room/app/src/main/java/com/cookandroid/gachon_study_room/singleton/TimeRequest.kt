package com.cookandroid.gachon_study_room.singleton

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.cookandroid.gachon_study_room.data.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object TimeRequest {

    fun time() : String{
        var cal = Calendar.getInstance()
        var hour = cal.get(Calendar.HOUR_OF_DAY)
        var interval = 10 - (cal.get(Calendar.MINUTE) % 10)
        var minute = cal.get(Calendar.MINUTE) + interval
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        return  SimpleDateFormat("HH시 mm분").format(cal.time)
    }

    fun endTime(): String{
        var cal = Calendar.getInstance()
        var hour = cal.get(Calendar.HOUR_OF_DAY) + 3
        var interval = 10 - (cal.get(Calendar.MINUTE) % 10)
        var minute = cal.get(Calendar.MINUTE) + interval
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        return  SimpleDateFormat("HH시 mm분").format(cal.time)
    }

    fun timeLong() : Time{
        var cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        var day = cal.get(Calendar.DAY_OF_MONTH)
        var hour = cal.get(Calendar.HOUR_OF_DAY)
        var interval = 10 - (cal.get(Calendar.MINUTE) % 10)
        var minute = cal.get(Calendar.MINUTE) + interval
        var time = Time(GregorianCalendar(year, month, day, hour, minute).timeInMillis, hour, minute)
        Log.d("TAG", GregorianCalendar(year, month, day, hour, minute).timeInMillis.toString())
        return time
    }

    fun endTimeLong(): Time{
        var cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        var day = cal.get(Calendar.DAY_OF_MONTH)
        var hour = cal.get(Calendar.HOUR_OF_DAY) + 3
        var interval = 10 - (cal.get(Calendar.MINUTE) % 10)
        var minute = cal.get(Calendar.MINUTE) + interval
        var time = Time(GregorianCalendar(year, month, day, hour, minute).timeInMillis, hour, minute)
        Log.d("TAG", GregorianCalendar(year, month, day, hour, minute).timeInMillis.toString())
        return time
    }


}