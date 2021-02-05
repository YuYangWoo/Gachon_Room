package com.cookandroid.gachon_study_room.singleton

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.cookandroid.gachon_study_room.data.LoginInformation
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject

object LoginVolly {
    private var loginInformation = LoginInformation("", "", "")
    private var msg: String = ""
    private lateinit var que: RequestQueue

    fun loginResult(context: Context, url: String, userId: String, password: String): String {
        que = Volley.newRequestQueue(context)
        val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    var jsonObject = JSONObject(response)
                    msg = jsonObject.getString("message")
                },
                Response.ErrorListener {error -> }) {

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["id"] = userId
                params["password"] = password
                return params
            }
        }
        que.add(stringRequest)
        return msg
    }

    fun loginAccount(context: Context, url: String, userId: String, password: String): LoginInformation {
        que = Volley.newRequestQueue(context)
        val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    var jsonObject = JSONObject(response)
                    var account = jsonObject.getJSONObject("account")
                    loginInformation.id = account.getString("id")
                    loginInformation.password = account.getString("password")
                    loginInformation.type = account.getString("type")
                },
                Response.ErrorListener { }) {

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["id"] = userId
                params["password"] = password
                return params
            }
        }
        que.add(stringRequest)
        return loginInformation
    }
}