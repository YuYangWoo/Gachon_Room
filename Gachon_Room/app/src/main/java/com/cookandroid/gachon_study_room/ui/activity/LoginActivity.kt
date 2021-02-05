package com.cookandroid.gachon_study_room.ui.activity

import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.LoginInformation
import com.cookandroid.gachon_study_room.databinding.ActivityLoginBinding
import com.cookandroid.gachon_study_room.singleton.LoginVolly
import com.cookandroid.gachon_study_room.ui.base.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private lateinit var que: RequestQueue
    private val url = "http://3.34.174.56:8080/login"
    private val TAG = "MAIN"
    private lateinit var message: String
    private lateinit var loginInformation: LoginInformation
    override fun init() {
        super.init()
        que = Volley.newRequestQueue(this)


        // 네트워크 연결이 안될 때
//        binding.btnLogin.setOnClickListener {
//           message = LoginVolly.loginResult(this, url, binding.edtId.text.toString(), binding.edtPassword.text.toString())
//            Log.d("test", message)
//            loginInformation = LoginVolly.loginAccount(this, url, binding.edtId.text.toString(), binding.edtPassword.text.toString())
//            Log.d("test", loginInformation.type)
//        }
    }

    override fun onStop() {
        super.onStop()
        if (que != null) {
            que.cancelAll(TAG)
        }
    }

}
