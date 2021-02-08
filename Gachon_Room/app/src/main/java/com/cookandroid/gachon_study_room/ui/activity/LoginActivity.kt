package com.cookandroid.gachon_study_room.ui.activity

import android.content.Intent
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.ActivityLoginBinding
import com.cookandroid.gachon_study_room.singleton.LoginRequest
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.base.BaseActivity
import com.cookandroid.gachon_study_room.ui.dialog.ProgressDialog

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private lateinit var que: RequestQueue
    private val url = "http://3.34.174.56:8080/login"
    private val TAG = "MAIN"
    override fun init() {
        super.init()
        que = Volley.newRequestQueue(this)
        btnLogin()
        checkBox()

        // 체크되어있다면 메인화면으로
        if (MySharedPreferences.getCheck(this)) {
            binding.edtId.setText(MySharedPreferences.getUserId(this))
            binding.edtPassword.setText(MySharedPreferences.getUserPass(this))
            MySharedPreferences.setUserId(this, binding.edtId.text.toString())
            MySharedPreferences.setUserPass(this, binding.edtPassword.text.toString())
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    // 체크박스가 체크되어있는지 체크되어 있지 않은지
    private fun checkBox() {
        binding.checkBox.setOnCheckedChangeListener { compoundButton, checked ->
            if (checked) {
                MySharedPreferences.setUserId(this, binding.edtId.text.toString())
                MySharedPreferences.setUserPass(this, binding.edtPassword.text.toString())
                MySharedPreferences.setCheck(this, binding.checkBox.isChecked)
            } else {
                MySharedPreferences.setCheck(this, binding.checkBox.isChecked)
                MySharedPreferences.clearUser(this)
            }
        }
    }

    // 로그인 버튼 클릭
    private fun btnLogin() {
        binding.btnLogin.setOnClickListener {
            LoginRequest.login(this, url, binding.edtId.text.toString(), binding.edtPassword.text.toString())
        }
    }

    // 큐 비우고 액티비티 종료
    override fun onStop() {
        super.onStop()
        if (que != null) {
            que.cancelAll(TAG)
        }
        finish()
    }

}