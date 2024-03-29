package com.cookandroid.gachon_study_room.data.singleton

import android.util.Log
import com.cookandroid.gachon_study_room.data.model.Time
import java.text.SimpleDateFormat
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

    // End 카드뷰 버튼 text set
    fun endTime(): String{
        var cal = Calendar.getInstance()
        var hour = cal.get(Calendar.HOUR_OF_DAY) + 4
        var interval = 10 - (cal.get(Calendar.MINUTE) % 10)
        var minute = cal.get(Calendar.MINUTE) + interval
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        return  SimpleDateFormat("HH시 mm분").format(cal.time)
    }

    fun timeLong() : Time {
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

    fun todayTime(): Long {
        var cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        var day = cal.get(Calendar.DAY_OF_MONTH)
        var hour = cal.get(Calendar.HOUR_OF_DAY)
        var interval = 10 - (cal.get(Calendar.MINUTE) % 10)
        var minute = cal.get(Calendar.MINUTE) + interval
        return GregorianCalendar(year, month, day, hour, minute).timeInMillis
    }

    // 좌석 그릴 때 4시간에 맞게끔 그리기 위한 시간 값
    fun endTimeLong(): Time {
        var cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        var day = cal.get(Calendar.DAY_OF_MONTH)
        var hour = cal.get(Calendar.HOUR_OF_DAY) + 4
        var interval = 10 - (cal.get(Calendar.MINUTE) % 10)
        var minute = cal.get(Calendar.MINUTE) + interval
        var time = Time(GregorianCalendar(year, month, day, hour, minute).timeInMillis, hour, minute)
        Log.d("TAG", GregorianCalendar(year, month, day, hour, minute).timeInMillis.toString())
        return time
    }

    fun statusTodayTime(): Int {
        var cal = Calendar.getInstance()
        var hour = cal.get(Calendar.HOUR_OF_DAY)
        cal.set(Calendar.HOUR_OF_DAY, hour)
        return  SimpleDateFormat("HH").format(cal.time).toInt()
    }
}