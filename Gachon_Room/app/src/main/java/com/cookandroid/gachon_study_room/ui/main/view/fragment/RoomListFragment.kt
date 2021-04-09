package com.cookandroid.gachon_study_room.ui.main.view.fragment

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.api.RetrofitBuilder
import com.cookandroid.gachon_study_room.data.model.room.RoomsData
import com.cookandroid.gachon_study_room.databinding.FragmentRoomListBinding
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.adapter.RoomAdapter
import com.cookandroid.gachon_study_room.ui.base.BaseBottomSheet
import com.cookandroid.gachon_study_room.ui.main.view.dialog.ProgressDialog
import com.cookandroid.gachon_study_room.ui.main.viewmodel.MainViewModel
import com.cookandroid.gachon_study_room.util.Resource
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class RoomListFragment : BaseBottomSheet<FragmentRoomListBinding>(R.layout.fragment_room_list) {
    private val dialog by lazy {
        ProgressDialog(requireContext())
    }
    private val viewModel: MainViewModel by sharedViewModel()
    private var TAG = "RoomListFragment"
    private var roomsData = RoomsData()
    override fun init() {
        super.init()
        initViewModel()
        cancel()
        binding.room = this
    }

    private fun initViewModel() {
        var input = HashMap<String, Any>()
        input["college"] = MySharedPreferences.getInformation(requireContext()).college
//        input["college"] = "TEST"
        viewModel.callRooms(input)
        viewModel.roomList.observe(viewLifecycleOwner, Observer { resource ->
            Log.d(TAG, resource.data.toString())
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    roomsData = resource.data!!
                    setRecyclerView()
                    dialog.dismiss()
                }
                Resource.Status.ERROR -> {
                    toast(requireContext(), resource.message + "\n" + resources.getString(R.string.connect_fail))
                    dialog.dismiss()
                }
                Resource.Status.LOADING -> {
                    dialog.show()
                }
            }
        })
    }
    private fun setRecyclerView() {
        with(binding.recyclerList) {
            adapter = RoomAdapter(dialog).apply {
                data = roomsData.rooms
                rooms = roomsData
                context = requireContext()
                notifyDataSetChanged()
            }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun cancel() {
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onStop() {
        super.onStop()
        dialog.dismiss()
    }

}