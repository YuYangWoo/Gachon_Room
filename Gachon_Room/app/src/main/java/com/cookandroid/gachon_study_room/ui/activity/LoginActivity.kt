package com.cookandroid.gachon_study_room.ui.activity

import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import android.widget.CompoundButton
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.LoginInformation
import com.cookandroid.gachon_study_room.databinding.ActivityLoginBinding
import com.cookandroid.gachon_study_room.isNetworkConnected
import com.cookandroid.gachon_study_room.singleton.LoginVolly
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
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
//        checkAutoLogin()

        btnLogin()
        // 체크되어있다면
        if (MySharedPreferences.getCheck(this)) {
            binding.edtId.setText(MySharedPreferences.getUserId(this))
            binding.edtPassword.setText(MySharedPreferences.getUserPass(this))
            binding.checkBox.isChecked = true
            startActivity(Intent(this, MainActivity::class.java))
        }
        else {
        }

        // 체크박스가 체크되어있는지 체크되어 있지 않은지
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
//
//    // 자동로그인 체크
//    private fun checkAutoLogin() {
//        // SharedPreferences 안에 값이 저장되어 있지 않을 때 -> Login
//        if (MySharedPreferences.getUserId(this).isNullOrBlank()
//                || MySharedPreferences.getUserPass(this).isNullOrBlank()) {
//            btnLogin()
//        } else { // SharedPreferences 안에 값이 저장되어 있을 때 -> MainActivity로 이동
//            toast("${MySharedPreferences.getUserId(this)}님 자동 로그인 되었습니다.")
//            MySharedPreferences.setUserId(this, binding.edtId.text.toString())
//            MySharedPreferences.setUserPass(this, binding.edtPassword.text.toString())
//            MySharedPreferences.setMyInformation(this,
//                    LoginVolly.loginInformation.id,
//                    LoginVolly.loginInformation.password,
//                    LoginVolly.loginInformation.department,
//                    LoginVolly.loginInformation.studentId,
//                    LoginVolly.loginInformation.type,
//                    LoginVolly.loginInformation.name,
//                    LoginVolly.loginInformation.imageURL
//            )
//
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }

    // 로그인 버튼 클릭
    private fun btnLogin() {
        binding.btnLogin.setOnClickListener {
            message = LoginVolly.getResult(this, url, binding.edtId.text.toString(), binding.edtPassword.text.toString())
            loginInformation = LoginVolly.getUser(this, url, binding.edtId.text.toString(), binding.edtPassword.text.toString())

            Log.d("test", message)
            Log.d("test", loginInformation.studentId)
            if (!isNetworkConnected(this)) {
                toast("인터넷 연결을 확인해주세요")
            } else if (binding.edtId.text.toString().isBlank() || binding.edtPassword.text.toString().isBlank()) {
                toast("계정을 확인해주세요")
            } else if (message == "INVALID_ACCOUNT") {
                toast("아이디와 비밀번호를 확인하세요")
            } else if (message == "SMART_GACHON_ERROR" || message == "ERROR") {
                toast("서버 에러입니다.")
            } else if (loginInformation.type == "STUDENT" && message == "SUCCESS") {
                toast("${MySharedPreferences.getUserId(this)}님 로그인 되었습니다.")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

        }
    }

    override fun onStop() {
        super.onStop()
        if (que != null) {
            que.cancelAll(TAG)
        }
    }

}