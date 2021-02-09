package com.cookandroid.gachon_study_room.ui.fragment

import android.content.Intent
import android.util.Log
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.FragmentMainBinding
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.singleton.PhotoRequest
import com.cookandroid.gachon_study_room.ui.activity.LoginActivity
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    override fun init() {
        super.init()
        Log.d("test", MySharedPreferences.getUserId(requireContext()))
        logout()
        http()
    }

    private fun http() {
        PhotoRequest(requireContext()).requestLogin()
    }

    private fun logout() {
        binding.btnLogout.setOnClickListener {
            MySharedPreferences.clearUser(requireContext())
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            toast("로그아웃 되었습니다.")
            requireActivity().finish()
        }
    }

}