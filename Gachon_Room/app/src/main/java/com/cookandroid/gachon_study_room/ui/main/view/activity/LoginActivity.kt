package com.cookandroid.gachon_study_room.ui.main.view.activity

import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.Information
import com.cookandroid.gachon_study_room.databinding.ActivityLoginBinding
import com.cookandroid.gachon_study_room.data.repository.LoginRepository
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.base.BaseActivity
import com.cookandroid.gachon_study_room.ui.main.view.dialog.ProgressDialog
import com.cookandroid.gachon_study_room.ui.main.viewmodel.LoginViewModel
import com.cookandroid.gachon_study_room.ui.main.viewmodel.LoginViewModelFactory
import com.cookandroid.gachon_study_room.util.Resource

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val TAG = "MAIN"
    private val viewModelFactory by lazy {
        LoginViewModelFactory(LoginRepository())
    }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }
    private var userData = Information()
    private var input = HashMap<String, Any>()
    private val dialog by lazy {
        ProgressDialog(this)
    }

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
            input["id"] = binding.edtId.text.toString()
            input["password"] = binding.edtPassword.text.toString()
            viewModel.loginApiCall(input)
        }
    }

    private fun initViewModel() {
        viewModel.loginData.observe(this@LoginActivity, Observer {
            it.let { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        userData = resource.data!!
                        loginApi()
                        dialog.dismiss()
                        Log.d(TAG, "성공")
                        Log.d(TAG,resource.data.toString())
                    }
                    Resource.Status.ERROR -> {
                        Log.d(TAG, "에러")

                       dialog.dismiss()
                    }
                    Resource.Status.LOADING -> {
                        Log.d(TAG, "로딩")

                        dialog.show()
                    }
//                    userData.result -> {
//                        loginApi()
//                        Log.d(TAG, "로그인성공")
//                        ProgressDialog(this).dismiss()
//                    }
//                    else -> {
//                        toast(this@LoginActivity, userData.response)
//                    }
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
            MySharedPreferences.setInformation(this@LoginActivity, userData.account.type, userData.account.department, userData.account.studentId, userData.account.studentName, userData.account.college)
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        } else {
            toast(this@LoginActivity, "연결실패")
        }
    }

}