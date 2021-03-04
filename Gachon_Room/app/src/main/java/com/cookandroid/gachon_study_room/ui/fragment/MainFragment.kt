package com.cookandroid.gachon_study_room.ui.fragment

import android.content.Intent
import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.adapter.MainAdapter
import com.cookandroid.gachon_study_room.data.StudentInformation
import com.cookandroid.gachon_study_room.databinding.FragmentMainBinding
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.singleton.RoomRequest
import com.cookandroid.gachon_study_room.singleton.RoomsRequest
import com.cookandroid.gachon_study_room.ui.activity.LoginActivity
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import kotlinx.coroutines.*

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val TAG = "MAIN"
    private lateinit var queue: RequestQueue
    val list: ArrayList<String> by lazy {
        arrayListOf(resources.getString(R.string.confirm_my_seat), resources.getString(R.string.choose_or_reservation), resources.getString(R.string.confirm_seat))
    }

    private val info: StudentInformation by lazy {
        MySharedPreferences.getInformation(requireContext())
    }

    override fun init() {
        super.init()
//        SessionRequest.request(requireContext(), binding.img)
        btnOption()
        binding.student = info
        setRecyclerView()
        roomInfo()
    }

    private fun roomInfo() {
        queue = Volley.newRequestQueue(context)
        RoomRequest.request(requireContext())
        RoomsRequest.request(requireContext())
    }

    private fun btnOption() {
        binding.btnOption.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToOptionFragment())
        }
    }

    private fun setRecyclerView() {
        with(binding.recyclerMain) {
            adapter = MainAdapter(requireContext()).apply {
                data = list
                notifyDataSetChanged()
            }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

}