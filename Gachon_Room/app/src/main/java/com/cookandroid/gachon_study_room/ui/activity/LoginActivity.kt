package com.cookandroid.gachon_study_room.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.Information
import com.cookandroid.gachon_study_room.databinding.ActivityLoginBinding
import com.cookandroid.gachon_study_room.service.RetrofitBuilder
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.base.BaseActivity
import com.cookandroid.gachon_study_room.ui.dialog.ProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val TAG = "MAIN"
    var userData = Information()
    override fun init() {
        super.init()
        btnLogin()
        checkBox()
        // 로그인요청을해주고 result가 true면 그때넘어가야함
        // 로그인할 때마다 가져온거.
        // 체크되어있다면 메인화면으로
        if (MySharedPreferences.getCheck(this)) {
            binding.edtId.setText(MySharedPreferences.getUserId(this))
            binding.edtPassword.setText(MySharedPreferences.getUserPass(this))
            binding.checkBox.isChecked = true
            loginApi()
        }
    }

    // 체크박스가 체크되어있는지 체크되어 있지 않은지
    private fun checkBox() {
        binding.checkBox.setOnCheckedChangeListener { compoundButton, checked ->
            if (checked) {
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
            loginApi()
        }
    }

    private fun loginApi() {
        val dialog = ProgressDialog(this@LoginActivity).apply {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
        var input = HashMap<String, String>()
        input["id"] = binding.edtId.text.toString()
        input["password"] = binding.edtPassword.text.toString()
        RetrofitBuilder.api.loginRequest(input).enqueue(object : Callback<Information> {
            override fun onResponse(call: Call<Information>, response: Response<Information>) {

                if (response.isSuccessful) {
                    userData = response.body()!!
                    // result가 실패할 경우
                    if (!userData.result) {
                        toast(this@LoginActivity, userData.response)
                    } else {
                        if (userData.account.type == "STUDENT" && userData.result) {
                            toast(this@LoginActivity, "${userData.account.id}님 ${resources.getString(R.string.confirm_login)}")
                            MySharedPreferences.setUserId(this@LoginActivity, binding.edtId.text.toString())
                            MySharedPreferences.setUserPass(this@LoginActivity, binding.edtPassword.text.toString())
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            MySharedPreferences.setInformation(this@LoginActivity, userData.account.type,userData.account.department, userData.account.studentId, userData.account.studentName, userData.account.college)
                            finish()
                        }

                    }
                    dialog.dismiss()
                }

            }

            override fun onFailure(call: Call<Information>, t: Throwable) {
                toast(this@LoginActivity, "연결 실패")
            }

        })
    }


}