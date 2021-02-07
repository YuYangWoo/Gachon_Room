package com.cookandroid.gachon_study_room.singleton

import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.cookandroid.gachon_study_room.data.LoginInformation
import kotlinx.coroutines.*
import org.json.JSONObject

object LoginVolly {
    private var loginInformation = LoginInformation("", "", "", "", "", "")
    private var msg: String = ""
    private lateinit var que: RequestQueue
    private lateinit var User: Job
    private lateinit var connect: Job
    private fun login(context: Context, url: String, userId: String, password: String) = runBlocking {

      connect = launch {
           que = Volley.newRequestQueue(context)
            val stringRequest: StringRequest = object : StringRequest(
                    Method.POST, url,
                    Response.Listener { response ->
                        var jsonObject = JSONObject(response)
                        var account = jsonObject.getJSONObject("account")

                        msg = jsonObject.getString("message")
                        Log.d("test", msg+"요청에서")
                        loginInformation.id = account.getString("id")
                        loginInformation.password = account.getString("password")
                        loginInformation.type = account.getString("type")
                        loginInformation.department = account.getString("department")
                        loginInformation.studentId = account.getString("studentId")
                        loginInformation.name = account.getString("name")
                    },
                    Response.ErrorListener { error -> error.printStackTrace()}) {
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

   fun getResult(context: Context, url: String, userId: String, password: String): String {

      return runBlocking {
                login(context, url, userId, password)
          connect.join()
                Log.d("test", "getResult" + msg)
         msg
        }
    }

    fun getUser(context: Context, url: String, userId: String, password: String): LoginInformation {
        return runBlocking {
            login(context, url, userId, password)
            connect.join()
            loginInformation
        }
    }

}