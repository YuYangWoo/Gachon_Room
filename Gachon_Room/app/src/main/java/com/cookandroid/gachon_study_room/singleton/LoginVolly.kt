package com.cookandroid.gachon_study_room.singleton

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

object LoginVolly {
    fun loginVolly(context: Context, url: String, userId: String, password: String) {
        val requestQueue = Volley.newRequestQueue(context)

        val jsonObj = JSONObject()
        jsonObj.put("id", userId)
        jsonObj.put("password", password)
        val request =
            JsonObjectRequest(Request.Method.POST, url, jsonObj,
                Response.Listener { response ->
                    try {
                        Toast.makeText(context, response["message"].toString(), Toast.LENGTH_LONG)
                            .show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error ->
                    Log.d("test", "잘못됨")
                    error.printStackTrace()
                })
        requestQueue.add(request)
    }
}