package com.cookandroid.gachon_study_room.singleton

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.cookandroid.gachon_study_room.data.Room
import com.cookandroid.gachon_study_room.data.RoomsData
import com.google.gson.Gson
import java.io.UnsupportedEncodingException

object RoomsRequest {
    private lateinit var queue: RequestQueue
    var room = RoomsData()
    private val url = "http://3.34.174.56:8080/rooms"
    fun request(context: Context) : RoomsData {
        queue = Volley.newRequestQueue(context)

        val stringRequest: StringRequest = object : StringRequest(Method.POST, url,
                Response.Listener {
                    room = Gson().fromJson(it, RoomsData::class.java)
                    Log.d("test", room.rooms[0].reserved[0].toString())
                }, Response.ErrorListener {
            Log.d("Error", it.toString())
        }) {
            // 서버로 넘길 값
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
//                params["college"] = MySharedPreferences.getInformation(context).college
                params["college"] = "TEST"

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
        return room
    }

    fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}