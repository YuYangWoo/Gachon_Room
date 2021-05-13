package com.cookandroid.gachon_study_room.data.singleton

import android.content.Context
import android.content.SharedPreferences
import com.cookandroid.gachon_study_room.data.model.Information
import com.cookandroid.gachon_study_room.data.model.Reserve
import com.cookandroid.gachon_study_room.data.model.room.Room

object MySharedPreferences {
    private val MY_ACCOUNT : String = "account"
    private var student: Information.Account = Information.Account()
    private var reservation = Room.Reservation()

    // 방정보 위치
    fun setPriorTime(context: Context, input: Long) {
        val prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putLong("MY_PRIOR", input)
        editor.commit()
    }

    fun getPriorTime(context: Context): Long {
        val prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getLong("MY_PRIOR", 0)
    }

    // 방정보 위치
    fun setRoomPosition(context: Context, input: Int) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putInt("MY_ROOM_POS", input)
        editor.commit()
    }

    fun getRoomPosition(context: Context): Int {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getInt("MY_ROOM_POS", 0)
    }

    // 사용자 Token
    fun setToken(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_TOKEN", input)
        editor.commit()
    }

    fun getToken(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("MY_TOKEN", "").toString()
    }

    // 사용자 예약정보 Set
    fun setReservation(context: Context, reservationData: Room.Reservation) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit().apply{
            putString("MY_COLLEGE", reservationData.college)
            putString("MY_ROOM_NAME", reservationData.roomName)
            putInt("MY_SEAT", reservationData.seat)
            putLong("MY_TIME", reservationData.time)
            putLong("MY_BEGIN", reservationData.begin)
            putLong("MY_END", reservationData.end)
            putBoolean("MY_CONFIRMED", reservationData.confirmed)
            putInt("MY_EXTENDED", reservationData.numberOfExtendTime)
            putString("MY_RESERVATION_ID", reservationData.reservationId)
        }
        editor.commit()
    }

    // 사용자 예약정보 얻기
    fun getReservation(context: Context): Room.Reservation{
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        with(reservation) {
            college = prefs.getString("MY_COLLEGE", "").toString()
            roomName = prefs.getString("MY_ROOM_NAME", "").toString()
            seat = prefs.getInt("MY_SEAT", 0)
            time = prefs.getLong("MY_TIME", 0L)
            begin = prefs.getLong("MY_BEGIN", 0L)
            end = prefs.getLong("MY_END", 0L)
            confirmed = prefs.getBoolean("MY_CONFIRMED", false)
            numberOfExtendTime = prefs.getInt("MY_EXTENDED", 0)
            reservationId = prefs.getString("MY_RESERVATION_ID", "").toString()

        }
        return reservation
    }

    // 사용자 정보 Set
    fun setInformation(context: Context, type: String, department: String, studentId: String, name: String, college: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit().apply{
            putString("MY_TYPE", type)
            putString("MY_DEPARTMENT", department)
            putString("MY_STUDENT_ID", studentId)
            putString("MY_NAME", name)
            putString("MY_COLLEGE", college)
        }
        editor.commit()
    }

    // 사용자 정보 얻기
    fun getInformation(context: Context): Information.Account{
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        with(student) {
            type = prefs.getString("MY_TYPE", "").toString()
            department = prefs.getString("MY_DEPARTMENT", "").toString()
            studentId = prefs.getString("MY_STUDENT_ID", "").toString()
            studentName = prefs.getString("MY_NAME", "").toString()
            college = prefs.getString("MY_COLLEGE", "").toString()
        }
        return student
    }

    // 사용자 Id Set
    fun setUserId(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_ID", input)
        editor.commit()
    }

    fun getUserId(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("MY_ID", "").toString()
    }

    fun setUserPass(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_PASS", input)
        editor.commit()
    }

    fun getUserPass(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("MY_PASS", "").toString()
    }

    fun clearUser(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }

    fun setCheck(context: Context, input: Boolean) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putBoolean("MY_AUTO_LOGIN", input)
        editor.commit()
    }

    fun getCheck(context: Context) : Boolean{
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getBoolean("MY_AUTO_LOGIN", false)
    }
}