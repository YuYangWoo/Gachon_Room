package com.cookandroid.gachon_study_room.singleton

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.cookandroid.gachon_study_room.data.LoginInformation
import com.cookandroid.gachon_study_room.isNetworkConnected
import com.cookandroid.gachon_study_room.ui.activity.MainActivity
import com.cookandroid.gachon_study_room.ui.dialog.ProgressDialog
import org.json.JSONObject

object LoginVolly {
    private var loginInformation = LoginInformation("", "", "", "", "", "")
    private var msg: String = ""
    private lateinit var que: RequestQueue

    fun login(context: Context, url: String, userId: String, password: String) {
        with(ProgressDialog(context)) {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }

        que = Volley.newRequestQueue(context)
        val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    var jsonObject = JSONObject(response)
                    var account = jsonObject.getJSONObject("account")
                    msg = jsonObject.getString("message")
                    loginInformation.id = account.getString("id")
                    loginInformation.password = account.getString("password")
                    loginInformation.type = account.getString("type")
                    loginInformation.department = account.getString("department")
                    loginInformation.studentId = account.getString("studentId")
                    loginInformation.name = account.getString("name")
                    ProgressDialog(context).dismiss()

                    if (!isNetworkConnected(context)) {
                        toast(context, "인터넷 연결을 확인해주세요")
                    } else if (userId.isBlank() || password.isBlank()) {
                        toast(context, "계정을 확인해주세요")
                    } else if (msg == "INVALID_ACCOUNT") {
                        toast(context, "아이디와 비밀번호를 확인하세요")
                    } else if (msg == "SMART_GACHON_ERROR" || msg == "ERROR") {
                        toast(context, "서버 에러입니다.")
                    } else if (loginInformation.type == "STUDENT" && msg == "SUCCESS") {
                        toast(context, "${MySharedPreferences.getUserId(context)}님 로그인 되었습니다.")
                        startActivity(context, Intent(context, MainActivity::class.java), null)
                    }

                },
                Response.ErrorListener { error -> error.printStackTrace() }) {

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