package com.cookandroid.gachon_study_room.ui.fragment

import android.content.Intent
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.StudentInformation
import com.cookandroid.gachon_study_room.databinding.FragmentMainBinding
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.activity.LoginActivity
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import kotlinx.coroutines.*

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
//    private lateinit var login: Job
//    private lateinit var job: Job
    private val info: StudentInformation by lazy {
        MySharedPreferences.getInformation(requireContext())
    }

    override fun init() {
        super.init()
        Log.d("test", MySharedPreferences.getUserId(requireContext()))

        binding.btnOption.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToOptionFragment())
        }
       binding.student = info
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