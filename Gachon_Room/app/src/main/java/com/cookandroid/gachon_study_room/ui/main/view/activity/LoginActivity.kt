package com.cookandroid.gachon_study_room.ui.main.view.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.Information
import com.cookandroid.gachon_study_room.databinding.ActivityLoginBinding
import com.cookandroid.gachon_study_room.data.api.RetrofitBuilder
import com.cookandroid.gachon_study_room.data.repository.LoginRepository
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.base.BaseActivity
import com.cookandroid.gachon_study_room.ui.main.view.dialog.ProgressDialog
import com.cookandroid.gachon_study_room.ui.main.viewmodel.LoginViewModel
import com.cookandroid.gachon_study_room.ui.main.viewmodel.LoginViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val TAG = "MAIN"
    private val viewModelFactory by lazy {
        LoginViewModelFactory(LoginRepository())
    }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }
    private var userData = Information()
    private lateinit var dialog: ProgressDialog
    override fun init() {
        super.init()
        checkAutoLogin()
        btnLogin()
        checkBox()
        initViewModel()
    }

    private fun checkAutoLogin() {
        if (MySharedPreferences.getCheck(this@LoginActivity) &&
                MySharedPreferences.getUserId(this@LoginActivity).isNotBlank() &&
                MySharedPreferences.getUserPass(this@LoginActivity).isNotBlank()) {
            binding.edtId.setText(MySharedPreferences.getUserId(this@LoginActivity))
            binding.edtPassword.setText(MySharedPreferences.getUserPass(this@LoginActivity))
            binding.checkBox.isChecked = true
            var input = HashMap<String, Any>()
            input["id"] = binding.edtId.text.toString()
            input["password"] = binding.edtPassword.text.toString()
            viewModel.loginApiCall(input)
        }
    }

    private fun initViewModel() {
        viewModel.loginData.observe(this@LoginActivity) {
            userData = it
            when {
                userData.result -> {
                    loginApi()
                    Log.d(TAG, "로그인성공")
                    ProgressDialog(this).dismiss()
                }
                else -> {
                    toast(this@LoginActivity, userData.response)
                }
            }
        }
    }

    // 체크박스가 체크되어있는지 체크되어 있지 않은지
    private fun checkBox() {
        binding.checkBox.setOnCheckedChangeListener { compoundButton, checked ->
            if (checked) {
                MySharedPreferences.setCheck(this@LoginActivity, binding.checkBox.isChecked)
            } else {
                MySharedPreferences.setCheck(this@LoginActivity, binding.checkBox.isChecked)
                MySharedPreferences.clearUser(this@LoginActivity)
            }
        }
    }

    // 로그인 버튼 클릭
    private fun btnLogin() {
        binding.btnLogin.setOnClickListener {
            var input = HashMap<String, Any>()
            input["id"] = binding.edtId.text.toString()
            input["password"] = binding.edtPassword.text.toString()
            viewModel.loginApiCall(input)
        }
    }

    private fun loginApi() {
        if (userData.account.type == "STUDENT" && userData.result) {
            toast(this@LoginActivity, "${userData.account.id}님 ${resources.getString(R.string.confirm_login)}")
            MySharedPreferences.setUserId(this@LoginActivity, binding.edtId.text.toString())
            MySharedPreferences.setUserPass(this@LoginActivity, binding.edtPassword.text.toString())
            MySharedPreferences.setInformation(this@LoginActivity, userData.account.type,userData.account.department, userData.account.studentId, userData.account.studentName, userData.account.college)
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        else {
            toast(this@LoginActivity, "연결실패")
        }
    }
}