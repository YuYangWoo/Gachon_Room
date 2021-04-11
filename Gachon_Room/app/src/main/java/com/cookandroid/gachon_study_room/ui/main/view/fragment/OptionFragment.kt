package com.cookandroid.gachon_study_room.ui.main.view.fragment

import android.content.Intent
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.FragmentOptionBinding
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.main.view.activity.LoginActivity
import com.cookandroid.gachon_study_room.ui.base.BaseFragment

class OptionFragment :BaseFragment<FragmentOptionBinding>(R.layout.fragment_option) {
    override fun init() {
        super.init()
        logout()
    }

    private fun logout() {
        binding.btnLogout.setOnClickListener {
            MySharedPreferences.clearUser(requireContext())
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            toast(requireContext(), "로그아웃 되었습니다.")
            requireActivity().finish()
        }
    }

}