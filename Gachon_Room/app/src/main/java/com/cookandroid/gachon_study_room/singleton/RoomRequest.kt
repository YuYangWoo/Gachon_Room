package com.cookandroid.gachon_study_room.singleton

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.LoginInformation
import com.cookandroid.gachon_study_room.data.RoomData
import com.cookandroid.gachon_study_room.isNetworkConnected
import com.cookandroid.gachon_study_room.ui.activity.MainActivity
import com.cookandroid.gachon_study_room.ui.dialog.ProgressDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URL

object RoomRequest {
    private lateinit var queue: RequestQueue
    private var roomInfo = RoomData("", "", JSONArray(), JSONArray(), JSONArray())

    fun request(context: Context, url: String) {
        queue = Volley.newRequestQueue(context)

        val stringRequest: StringRequest = object : StringRequest(Method.GET, url,
                Response.Listener {
                    var jsonObject = JSONObject(it)

                    var room = jsonObject.getJSONObject("room")
                    roomInfo.college = room.getString("college")
                    roomInfo.name = room.getString("name")
                    roomInfo.seat = room.getJSONArray("seat")
                    roomInfo.reserved = room.getJSONArray("reserved")
                    roomInfo.available = room.getJSONArray("available")
                    Log.d("test", roomInfo.seat.length().toString())
                    var result = jsonObject.getString("result")
                }, Response.ErrorListener {
            Log.d("Error", it.toString())
        }) {


        }
        queue.add(stringRequest)
    }

    fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}