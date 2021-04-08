package com.cookandroid.gachon_study_room.ui.main.view.fragment

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.Information
import com.cookandroid.gachon_study_room.databinding.FragmentMainBinding
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.adapter.MainAdapter
import com.cookandroid.gachon_study_room.ui.base.BaseFragment

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val TAG = "MAIN"
    val list: ArrayList<String> by lazy {
        arrayListOf(resources.getString(R.string.confirm_my_seat), resources.getString(R.string.choose_or_reservation), resources.getString(R.string.confirm_seat))
    }

    // 뷰모델로바꾸기
    private val info: Information.Account by lazy {
        MySharedPreferences.getInformation(requireContext())
    }

    override fun init() {
        super.init()
        btnOption()
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
            adapter = MainAdapter(requireContext()).apply {
                data = list
                notifyDataSetChanged()
            }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }


}