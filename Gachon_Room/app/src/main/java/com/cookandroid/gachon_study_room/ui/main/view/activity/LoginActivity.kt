package com.cookandroid.gachon_study_room.ui.main.view.activity

import android.content.Intent
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.Account
import com.cookandroid.gachon_study_room.data.model.LoginRequest
import com.cookandroid.gachon_study_room.databinding.ActivityLoginBinding
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.base.BaseActivity
import com.cookandroid.gachon_study_room.ui.main.view.dialog.ProgressDialog
import com.cookandroid.gachon_study_room.ui.main.viewmodel.LoginViewModel
import com.cookandroid.gachon_study_room.util.Resource
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val TAG = "LOGIN_ACTIVITY"
    private val model: LoginViewModel by viewModel()
    private var userData = Account()
    private var input = LoginRequest()
    private val dialog by lazy {
        ProgressDialog(this)
    }

    override fun init() {
        super.init()
        checkAutoLogin()
        btnLogin()
        checkBox()
    }

    // 자동 로그인
    private fun checkAutoLogin() {
        if (MySharedPreferences.getCheck(this@LoginActivity) &&
            MySharedPreferences.getUserId(this@LoginActivity).isNotBlank() &&
            MySharedPreferences.getUserPass(this@LoginActivity).isNotBlank()
        ) {
            binding.edtId.editText!!.setText(MySharedPreferences.getUserId(this@LoginActivity))
            binding.edtPassword.editText!!.setText(MySharedPreferences.getUserPass(this@LoginActivity))
            binding.checkBox.isChecked = true
            input.id = binding.edtId.editText!!.text.toString()
            input.password = binding.edtPassword.editText!!.text.toString()
            initViewModel()
        }
    }

    // 로그인 서버 API 통신
    private fun initViewModel() {
        model.loginApiCall(input).observe(this@LoginActivity, Observer { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    dialog.dismiss()
                    when (resource.data!!.code()) {
                        200 -> {
                            Log.d(TAG, "initViewModel: ${resource.data!!}")
                            userData = resource.data.body()!!
                            loginApi()
                        }
                        else -> {
                            toast(this, "에러요")
                        }
                    }
                }
                Resource.Status.LOADING -> {
                    dialog.show()
                }
                Resource.Status.ERROR -> {
                    toast(
                        this,
                        resource.message + "\n" + resources.getString(R.string.connect_fail)
                    )
                    dialog.dismiss()
                }
            }
        })
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
//            input["id"] = binding.edtId.editText!!.text.toString()
//            input["password"] = binding.edtPassword.editText!!.text.toString()
            input.id = binding.edtId.editText!!.text.toString()
            input.password = binding.edtPassword.editText!!.text.toString()
            initViewModel()
        }
    }

    // 로그인 체크
    private fun loginApi() {
        if (userData.type == "STUDENT") {
            toast(
                this@LoginActivity,
                "${userData.id}님 ${resources.getString(R.string.confirm_login)}"
            )
            MySharedPreferences.setUserId(this@LoginActivity, binding.edtId.editText!!.text.toString())
            MySharedPreferences.setUserPass(this@LoginActivity, binding.edtPassword.editText!!.text.toString())
            MySharedPreferences.setInformation(
                this@LoginActivity,
                userData.type,
                userData.department,
                userData.studentId,
                userData.studentName,
                userData.college
            )
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

}