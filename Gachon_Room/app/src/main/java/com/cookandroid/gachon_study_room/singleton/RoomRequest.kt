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
    private val url = "http://3.34.174.56:8080/room"
    fun request(context: Context) {
        queue = Volley.newRequestQueue(context)

        val stringRequest: StringRequest = object : StringRequest(Method.POST, url,
                Response.Listener {
                    var jsonObject = JSONObject(it)
                    Log.d("test", jsonObject.toString())
//                    var room = jsonObject.getJSONObject("room")
//                    with(roomInfo) {
//                        college = room.getString("college")
//                        name = room.getString("name")
//                        seat = room.getJSONArray("seat")
//                        reserved = room.getJSONArray("reserved")
//                        available = room.getJSONArray("available")
//                    }
                }, Response.ErrorListener {
            Log.d("Error", it.toString())
        }) {
            // 서버로 넘길 값
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["college"] = "TEST"
                params["name"] = "Test"
                return params
            }

            // response를 UTF8로 변경해주는 소스코드
            override fun parseNetworkResponse(response: NetworkResponse): Response<String?>? {
                return try {
                    val utf8String = String(response.data, charset("UTF-8"))
                    Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    // log error
                    Response.error(ParseError(e))
                } catch (e: Exception) {
                    // log error
                    Response.error(ParseError(e))
                }
            }

        }
        queue.add(stringRequest)
    }

    fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}