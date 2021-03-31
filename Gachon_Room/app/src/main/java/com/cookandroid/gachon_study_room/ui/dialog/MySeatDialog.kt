package com.cookandroid.gachon_study_room.ui.dialog

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.MySeat
import com.cookandroid.gachon_study_room.databinding.FragmentMySeatBinding
import com.cookandroid.gachon_study_room.service.RetrofitBuilder
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.singleton.TimeRequest
import com.cookandroid.gachon_study_room.ui.base.BaseBottomSheet
import com.cookandroid.gachon_study_room.ui.fragment.ReservationFragment
import com.cookandroid.gachon_study_room.ui.fragment.ReservationFragment.Companion.simple
import com.cookandroid.gachon_study_room.ui.fragment.ReservationFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MySeatDialog : BaseBottomSheet<FragmentMySeatBinding>(R.layout.fragment_my_seat) {
    private lateinit var mySeatData: MySeat
    override fun init() {
        super.init()
        binding.txtSeatNumber.visibility = View.GONE
        binding.txtLocation.visibility = View.GONE
        binding.txtSeat.text = "예약을 진행해주세요."
        binding.txtTime.visibility = View.GONE
        binding.button.visibility = View.GONE
        binding.button2.visibility = View.GONE
        txtSet()
        back()
    }

    private fun txtSet() {
        var input = HashMap<String, String>()
        input["id"] = MySharedPreferences.getUserId(requireContext())
        input["password"] = MySharedPreferences.getUserPass(requireContext())
        RetrofitBuilder.api.mySeatRequest(input).enqueue(object : Callback<MySeat> {
            override fun onResponse(call: Call<MySeat>, response: Response<MySeat>) {
                mySeatData = response.body()!!

                if (mySeatData.reservations.isNotEmpty()) {
                    with(binding) {
                        txtSeatNumber.visibility = View.VISIBLE
                        txtLocation.visibility = View.VISIBLE
                        txtSeat.text = "나의자리"
                        txtTime.visibility = View.VISIBLE
                        button.visibility = View.VISIBLE
                        button2.visibility = View.VISIBLE
                    }

                    Log.d("TAG", mySeatData.toString())
                    binding.txtSeatNumber.text = binding.txtSeatNumber.text.toString() + " " + mySeatData.reservations[0].seat + "번"
                    binding.txtLocation.text = binding.txtLocation.text.toString() + " " + mySeatData.reservations[0].college + mySeatData.reservations[0].room
                    var date = Date()
                    date.time = mySeatData.reservations[0].begin
                    var start = simple.format(date)
                    date.time = mySeatData.reservations[0].end
                    var end = simple.format(date)
                    binding.txtTime.text = binding.txtTime.text.toString() + " " + "$start ~ $end"
                } else {
                    with(binding) {
                        txtSeatNumber.visibility = View.GONE
                        txtLocation.visibility = View.GONE
                        txtSeat.text = "예약을 진행해주세요."
                        txtTime.visibility = View.GONE
                        button.visibility = View.GONE
                        button2.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<MySeat>, t: Throwable) {
                Log.d("Error", "mySeat 연결 실패 $t")
            }

        })
    }

    private fun back() {
        binding.button2.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("반납확인").setMessage("좌석을 반납하시겠습니까?")
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                        var input = HashMap<String, Any>()
                        input["studentId"] = MySharedPreferences.getInformation(requireContext()).studentId
                        input["college"] = MySharedPreferences.getInformation(requireContext()).college
                        input["room"] = mySeatData.reservations[0].room
                        input["seat"] = mySeatData.reservations[0].seat
                        input["time"] = mySeatData.reservations[0].time
                        input["begin"] = mySeatData.reservations[0].begin
                        input["end"] = mySeatData.reservations[0].end
                        input["id"] = MySharedPreferences.getUserId(requireContext())
                        input["password"] = MySharedPreferences.getUserPass(requireContext())
                        RetrofitBuilder.api.back(input).enqueue(object : Callback<MySeat> {
                            override fun onResponse(call: Call<MySeat>, response: Response<MySeat>) {
                                if (response.isSuccessful) {
                                    toast(requireContext(), "반납성공!!")
                                    dismiss()
                                }
                            }

                            override fun onFailure(call: Call<MySeat>, t: Throwable) {
                                toast(requireContext(), "반납실패!!")

                            }

                        })
                    })
                    .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                        Log.d("TAG", "취소")
                    })
            builder.create()
            builder.show()

        }
    }

    companion object {
        var simple = SimpleDateFormat("HH시 mm분")
    }
}