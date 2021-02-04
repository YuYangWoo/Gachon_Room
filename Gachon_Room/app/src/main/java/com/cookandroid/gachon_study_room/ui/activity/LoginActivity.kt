package com.cookandroid.gachon_study_room.ui.activity

import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.ActivityLoginBinding
import com.cookandroid.gachon_study_room.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject


class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private lateinit var jsonObj: JSONObject
    private lateinit var que: RequestQueue
    private val TAG = "MAIN"
    override fun init() {
        super.init()
        val url = "http://3.34.174.56:8080/login"
        que = Volley.newRequestQueue(this)

        binding.btnLogin.setOnClickListener {
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    txtContent.text = response
                },
                Response.ErrorListener { }) {

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["id"] = edtId.text.toString()
                    params["password"] = edtPassword.text.toString()
                    return params
                }
            }
            que.add(stringRequest);
        }
    }

    override fun onStop() {
        super.onStop()
        if (que != null) {
            que.cancelAll(TAG)
        }
    }

}
