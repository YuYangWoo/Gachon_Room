package com.cookandroid.gachon_study_room.singleton

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimeRequest {

    @RequiresApi(Build.VERSION_CODES.O)
    fun day() : String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return current.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun time() : String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return current.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fullTime(): String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss초")
        return current.format(formatter)
    }
}