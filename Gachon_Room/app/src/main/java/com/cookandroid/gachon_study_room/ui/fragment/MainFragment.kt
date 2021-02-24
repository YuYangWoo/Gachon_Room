package com.cookandroid.gachon_study_room.ui.fragment

import android.content.Intent
import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.adapter.MainAdapter
import com.cookandroid.gachon_study_room.data.StudentInformation
import com.cookandroid.gachon_study_room.databinding.FragmentMainBinding
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.singleton.RoomRequest
import com.cookandroid.gachon_study_room.ui.activity.LoginActivity
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import kotlinx.coroutines.*

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    val list: ArrayList<String> by lazy {
        arrayListOf(resources.getString(R.string.confirm_my_seat), resources.getString(R.string.choose_or_reservation), resources.getString(R.string.confirm_seat))
    }

    private val info: StudentInformation by lazy {
        MySharedPreferences.getInformation(requireContext())
    }

    override fun init() {
        super.init()

        btnOption()

//        SessionRequest.request(requireContext(), binding.img)
        binding.student = info
        setRecyclerView()
    }

    private fun btnOption() {
        binding.btnOption.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToOptionFragment())
        }
    }

    private fun setRecyclerView() {
        with(binding.recyclerMain) {
            adapter = MainAdapter().apply {
                data = list
                notifyDataSetChanged()
            }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
//    private fun http() = runBlocking {
//       login = CoroutineScope(Dispatchers.IO).launch { PhotoRequest(requireContext()).requestLogin() }
//        login.join()
//        job = CoroutineScope(Dispatchers.Default).launch{SessionRequest().request()}
//        job.join()
//        Glide.with(requireContext()).load("https://info.gachon.ac.kr/StuCommonInfo/doGetPhoto.do?CALL_PAGE=STASTA_SHJSHJ09shj0901e&STUDENT_NO=201636010&p=826").into(binding.img)
//
//    }


}