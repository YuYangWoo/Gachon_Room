package com.cookandroid.gachon_study_room.ui.main.view.activity

import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.Information
import com.cookandroid.gachon_study_room.databinding.ActivityLoginBinding
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.base.BaseActivity
import com.cookandroid.gachon_study_room.ui.main.view.dialog.ProgressDialog
import com.cookandroid.gachon_study_room.ui.main.viewmodel.LoginViewModel
import com.cookandroid.gachon_study_room.util.Resource
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val TAG = "MAIN"
    private val model: LoginViewModel by viewModel()
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
            MySharedPreferences.getUserPass(this@LoginActivity).isNotBlank()
        ) {
            binding.edtId.setText(MySharedPreferences.getUserId(this@LoginActivity))
            binding.edtPassword.setText(MySharedPreferences.getUserPass(this@LoginActivity))
            binding.checkBox.isChecked = true
            input["id"] = binding.edtId.text.toString()
            input["password"] = binding.edtPassword.text.toString()
            model.loginApiCall(input)
        }
    }

    private fun initViewModel() {
        model.loginData.observe(this@LoginActivity, Observer {
            it.let { resource ->
                Log.d(TAG, resource.data.toString())
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        userData = resource.data!!
                        when (userData.result) {
                            true -> {
                                loginApi()
                            }
                            false -> {
                                toast(this@LoginActivity, userData.response)
                            }
                        }
                        dialog.dismiss()
                    }
                    Resource.Status.ERROR -> {
                        toast(this@LoginActivity, resource.message + "\n" + resources.getString(R.string.connect_fail))
                        dialog.dismiss()
                    }
                    Resource.Status.LOADING -> {
                        dialog.show()
                    }
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
            model.loginApiCall(input)
        }
    }

    private fun loginApi() {
        if (userData.account.type == "STUDENT" && userData.result) {
            toast(
                this@LoginActivity,
                "${userData.account.id}님 ${resources.getString(R.string.confirm_login)}"
            )
            MySharedPreferences.setUserId(this@LoginActivity, binding.edtId.text.toString())
            MySharedPreferences.setUserPass(this@LoginActivity, binding.edtPassword.text.toString())
            MySharedPreferences.setInformation(
                this@LoginActivity,
                userData.account.type,
                userData.account.department,
                userData.account.studentId,
                userData.account.studentName,
                userData.account.college
            )
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

}