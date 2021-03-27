package com.cookandroid.gachon_study_room.ui.dialog

import android.util.Log
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.MySeat
import com.cookandroid.gachon_study_room.databinding.FragmentMySeatBinding
import com.cookandroid.gachon_study_room.service.RetrofitBuilder
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.base.BaseBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySeatDialog : BaseBottomSheet<FragmentMySeatBinding>(R.layout.fragment_my_seat) {
    override fun init() {
        super.init()

//        var input = HashMap<String, String>()
//        input["id"] = MySharedPreferences.getUserId(requireContext())
//        input["password"] = MySharedPreferences.getUserPass(requireContext())
//        RetrofitBuilder.api.mySeatRequest(input).enqueue(object : Callback<MySeat> {
//            override fun onResponse(call: Call<MySeat>, response: Response<MySeat>) {
//                if(response.isSuccessful) {
//                    var mySeatData = response.body()!!
//                    Log.d("TAG", mySeatData.toString())
//                    binding.txtNumber.text = mySeatData.reservations[0].studentId
//                    binding.txtLocation.text = mySeatData.reservations[0].college + mySeatData.reservations[0].room
//                    binding.txtTime.text = "${mySeatData.reservations[0].begin} ~ ${mySeatData.reservations[0].end}"
//                }
//            }
//
//            override fun onFailure(call: Call<MySeat>, t: Throwable) {
//                Log.d("Error", "mySeat 연결 실패 $t")
//            }
//
//        })
    }
}