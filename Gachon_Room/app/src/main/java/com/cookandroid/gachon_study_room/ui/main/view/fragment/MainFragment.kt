package com.cookandroid.gachon_study_room.ui.main.view.fragment

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.Information
import com.cookandroid.gachon_study_room.databinding.FragmentMainBinding
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.data.singleton.TimeRequest
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import com.cookandroid.gachon_study_room.ui.main.view.dialog.ProgressDialog

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val TAG = "MAIN"
    private val info: Information.Account by lazy {
        MySharedPreferences.getInformation(requireContext())
    }
    private val dialog by lazy {
        ProgressDialog(requireContext())
    }
    override fun init() {
        super.init()
        btnOption()
        btnClick()
        binding.student = info

    }

    private fun btnOption() {
        binding.btnOption.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToOptionFragment())
        }
    }

    private fun btnClick() {
        binding.btnMySeat.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToMySeatDialog())
        }

        binding.btnReservation.setOnClickListener {
            RoomListFragment().show((context as AppCompatActivity).supportFragmentManager,"Modal")
        }

        binding.btnConfirm.setOnClickListener {
            binding.root.findNavController().navigate(MainFragmentDirections.actionMainFragmentToQrCodeFragment())
        }

    }

}