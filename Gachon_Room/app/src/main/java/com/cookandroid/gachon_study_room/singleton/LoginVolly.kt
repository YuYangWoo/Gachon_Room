package com.cookandroid.gachon_study_room.singleton

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.cookandroid.gachon_study_room.data.LoginInformation
import kotlinx.coroutines.*
import org.json.JSONObject

object LoginVolly {
    private var loginInformation = LoginInformation("", "", "")
    private var msg: String = ""
    private lateinit var que: RequestQueue
    private lateinit var connect: Job

    fun login(context: Context, url: String, userId: String, password: String) = runBlocking {
        que = Volley.newRequestQueue(context)
        connect = launch {
            val stringRequest: StringRequest = object : StringRequest(
                    Method.POST, url,
                    Response.Listener { response ->
                        var jsonObject = JSONObject(response)
                        var account = jsonObject.getJSONObject("account")

                        msg = jsonObject.getString("message")
                        loginInformation.id = account.getString("id")
                        loginInformation.password = account.getString("password")
                        loginInformation.type = account.getString("type")
                    },
                    Response.ErrorListener { error -> }) {

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["id"] = userId
                    params["password"] = password
                    return params
                }
            }
            que.add(stringRequest)
        }
    }

    fun getResult(): String {
        return runBlocking {
            connect.join()
            msg
        }
    }

    fun getUser(): LoginInformation {
        return runBlocking {
            connect.join()
            loginInformation
        }
    }

}