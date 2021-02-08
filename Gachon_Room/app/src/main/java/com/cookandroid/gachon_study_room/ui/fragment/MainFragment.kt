package com.cookandroid.gachon_study_room.ui.fragment

import android.content.Intent
import android.util.Log
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.FragmentMainBinding
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.activity.LoginActivity
import com.cookandroid.gachon_study_room.ui.base.BaseFragment

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    override fun init() {
        super.init()
        logout()
        var department = MySharedPreferences.getInformation(requireContext()).department
        Log.d("test", MySharedPreferences.getInformation(requireContext()).department)
        Log.d("test", MySharedPreferences.getInformation(requireContext()).studentId)

        Log.d("test", MySharedPreferences.getInformation(requireContext()).name)
        toast( MySharedPreferences.getInformation(requireContext()).name)


    }

    private fun logout() {
        binding.btnLogout.setOnClickListener {
            MySharedPreferences.clearUser(requireContext())
            startActivity(Intent(requireActivity(),LoginActivity::class.java))
            toast("로그아웃 되었습니다.")
            requireActivity().finish()
        }
    }
}