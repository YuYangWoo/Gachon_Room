package com.cookandroid.gachon_study_room

import android.content.Context
import android.net.ConnectivityManager
import java.lang.Exception

fun isNetworkConnected(context: Context): Boolean = try {
    (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo != null
} catch (e: Exception) {
    false
}