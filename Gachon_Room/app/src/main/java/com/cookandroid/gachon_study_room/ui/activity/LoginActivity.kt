package com.cookandroid.gachon_study_room.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.Account
import com.cookandroid.gachon_study_room.data.Information
import com.cookandroid.gachon_study_room.databinding.ActivityLoginBinding
import com.cookandroid.gachon_study_room.isNetworkConnected
import com.cookandroid.gachon_study_room.service.RetrofitBuilder
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.base.BaseActivity
import com.cookandroid.gachon_study_room.ui.dialog.ProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private lateinit var que: RequestQueue
    private val TAG = "MAIN"
    var userData = Information()
    override fun init() {
        super.init()
        que = Volley.newRequestQueue(this)
        btnLogin()
        checkBox()

        // 체크되어있다면 메인화면으로
        if (MySharedPreferences.getCheck(this) && MySharedPreferences.getResult(this)) {
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
//            LoginRequest.login(this, binding.edtId.text.toString(), binding.edtPassword.text.toString())
            var input = HashMap<String, String>()
            input["id"] = binding.edtId.text.toString()
            Log.d("test", binding.edtId.text.toString() + binding.edtPassword.text.toString())
            input["password"] = binding.edtPassword.text.toString()
            RetrofitBuilder.api.loginRequest(input).enqueue(object: Callback<Information> {
                override fun onResponse(call: Call<Information>, response: Response<Information>) {
                    val dialog = ProgressDialog(this@LoginActivity).apply{
                        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        show()
                    }
                    if(response.isSuccessful) {
                        userData.message = response.body()!!.message
                        userData.result = response.body()!!.result
                        userData.accout = response.body()!!.accout
                        Log.d("test", response.body()!!.accout.college)
                        // result가 실패할 경우
//                        if(!userData.result) {
//                            if (!isNetworkConnected(this@LoginActivity)) {
//                            toast(this@LoginActivity,resources.getString(R.string.confirm_internet)
//                            )
//                        } else if (input["id"]!!.isBlank() || input["password"]!!.isBlank()) {
//                           toast(
//                                this@LoginActivity,
//                                resources.getString(R.string.confirm_account)
//                            )
//                        } else if (userData.message == "INVALID_ACCOUNT") {
//                           toast(
//                                this@LoginActivity,
//                                resources.getString(R.string.confirm_id)
//                            )
//                        } else if (userData.message == "SMART_GACHON_ERROR" || userData.message == "ERROR") {
//                           toast(
//                                this@LoginActivity,
//                                resources.getString(R.string.server_error)
//                            )
//                        } else {
//                           toast(this@LoginActivity, "연결 실패")
//                        }
//                        }
//                        else {
//
//                        if (userData.accout.type == "STUDENT" && userData.result) {
//                            toast(
//                                this@LoginActivity,
//                                userData.accout.id + resources.getString(R.string.confirm_login)
//                            )
//                            MySharedPreferences.setResult(this@LoginActivity, true)
//                            startActivity(Intent(this@LoginActivity, MainActivity::class.java)
//                            )
//                        }
//
//                    }
//                    MySharedPreferences.setInformation(this@LoginActivity, userData.accout.department, userData.accout.studentId, userData.accout.name, userData.accout.college)
//                    dialog.dismiss()
                        }

                    }

                override fun onFailure(call: Call<Information>, t: Throwable) {
                    Log.d("test", "연결실패")
                }

            })
        }
    }

    // 큐 비우고 액티비티 종료
    override fun onStop() {
        super.onStop()

        finish()
    }

}