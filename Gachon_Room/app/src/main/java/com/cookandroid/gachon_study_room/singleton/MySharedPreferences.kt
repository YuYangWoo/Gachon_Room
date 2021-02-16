package com.cookandroid.gachon_study_room.singleton

import android.content.Context
import android.content.SharedPreferences
import com.cookandroid.gachon_study_room.data.StudentInformation

object MySharedPreferences {
    private val MY_ACCOUNT : String = "account"
    private var student: StudentInformation = StudentInformation("","","")

    fun setInformation(context: Context, department: String, studentId: String, name: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_DEPARTMENT", department)
        editor.putString("MY_STUDENT_ID", studentId)
        editor.putString("MY_NAME", name)
        editor.commit()
    }

    fun getInformation(context: Context): StudentInformation{
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        student.department = prefs.getString("MY_DEPARTMENT", "").toString()
        student.studentId = prefs.getString("MY_STUDENT_ID", "").toString()
        student.name = prefs.getString("MY_NAME", "").toString()
        return student
    }

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