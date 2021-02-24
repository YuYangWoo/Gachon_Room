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

                    // 보면 room자체가 jsonObject이기 때문에 jsonobject로 초기화
                    var room = jsonObject.getJSONObject("room")
                    roomInfo.college = room.getString("college")
                    roomInfo.name = room.getString("name")

                    // seat하고 reserved같은거는 jsonArray야 jsonArray타입 으로 받으면되
                    roomInfo.seat = room.getJSONArray("seat")
                    roomInfo.reserved = room.getJSONArray("reserved")
                    roomInfo.available = room.getJSONArray("available")

                    // 그냥 스트링은 getString으로 받고
                    var result = jsonObject.getString("result")
                    Log.d("test", room.toString())
                    Log.d("test", result)
                    Log.d("test", roomInfo.college)
                    Log.d("test", roomInfo.seat.toString())
                    Log.d("test", roomInfo.reserved.toString())

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