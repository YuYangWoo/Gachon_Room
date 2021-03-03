package com.cookandroid.gachon_study_room.singleton

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.GsonLogin
import com.cookandroid.gachon_study_room.data.LoginInformation
import com.cookandroid.gachon_study_room.isNetworkConnected
import com.cookandroid.gachon_study_room.ui.activity.MainActivity
import com.cookandroid.gachon_study_room.ui.dialog.ProgressDialog
import com.google.gson.Gson
import org.json.JSONObject
import java.io.UnsupportedEncodingException


object LoginRequest {
    private var loginInformation = LoginInformation("", "", "", "", "", "", "")
    private var msg: String = ""
    private var result: Boolean = false
    private lateinit var que: RequestQueue
    private val url = "http://3.34.174.56:8080/login"

    fun login(context: Context, userId: String, password: String) {
        val dialog = ProgressDialog(context).apply{
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }

        que = Volley.newRequestQueue(context)

        // 연결 성공, 실패 Listener
        val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->

                    var jsonObject = JSONObject(response)
                    var account = jsonObject.getJSONObject("account")
                    msg = jsonObject.getString("message")
                    result = jsonObject.getBoolean("result")

                    with(loginInformation) {
                        id = account.getString("id")
                        this.password = account.getString("password")
                        type = account.getString("type")
                        department = account.getString("department")
                        studentId = account.getString("studentId")
                        name = account.getString("name")
                        college = account.getString("college")
                    }

                    MySharedPreferences.setInformation(context, loginInformation.department, loginInformation.studentId, loginInformation.name, loginInformation.college)
                    dialog.dismiss()

                    if (!isNetworkConnected(context)) {
                        toast(context, context.resources.getString(R.string.confirm_internet))
                    } else if (userId.isBlank() || password.isBlank()) {
                        toast(context, context.resources.getString(R.string.confirm_account))
                    } else if (msg == "INVALID_ACCOUNT") {
                        toast(context, context.resources.getString(R.string.confirm_id))
                    } else if (msg == "SMART_GACHON_ERROR" || msg == "ERROR") {
                        toast(context, context.resources.getString(R.string.server_error))
                    } else if (loginInformation.type == "STUDENT" && result) {
                        toast(context, loginInformation.id + context.resources.getString(R.string.confirm_login))
                        startActivity(context, Intent(context, MainActivity::class.java), null)
                    }

                },
                Response.ErrorListener { error -> error.printStackTrace() }) {

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

            // 서버로 넘길 값
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

    fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}