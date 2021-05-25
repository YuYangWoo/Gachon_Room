package com.cookandroid.gachon_study_room.ui.main.view.fragment

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.FragmentOptionBinding
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.adapter.OptionAdapter
import com.cookandroid.gachon_study_room.ui.main.view.activity.LoginActivity
import com.cookandroid.gachon_study_room.ui.base.BaseFragment

class OptionFragment :BaseFragment<FragmentOptionBinding>(R.layout.fragment_option) {

    override fun init() {
        super.init()
        logout()
        with(binding.recyclerOptionList) {
            adapter = OptionAdapter()
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun logout() {
        binding.btnLogout.setOnClickListener {
            MySharedPreferences.clearUser(requireContext())
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            toast(requireContext(), resources.getString(R.string.logout_success))
            requireActivity().finish()
        }
    }

}