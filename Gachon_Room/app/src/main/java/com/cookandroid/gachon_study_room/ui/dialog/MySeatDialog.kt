package com.cookandroid.gachon_study_room.ui.dialog

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
    private lateinit var dialog: ProgressDialog
    private var TAG = "MYSEAT"
    override fun init() {
        super.init()
        dialog = ProgressDialog(requireContext()).apply {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
        with(binding) {
            txtSeatNumber.visibility = View.GONE
            txtLocation.visibility = View.GONE
            txtSeat.visibility = View.GONE
            txtTime.visibility = View.GONE
            button.visibility = View.GONE
            button2.visibility = View.GONE
            txtStatus.visibility = View.GONE
        }
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

                // 예약을 했다면
                if (mySeatData.reservations.isNotEmpty()) {
                    with(binding) {
                        txtSeatNumber.visibility = View.VISIBLE
                        txtLocation.visibility = View.VISIBLE
                        txtSeat.visibility = View.VISIBLE
                        txtTime.visibility = View.VISIBLE
                        button.visibility = View.VISIBLE
                        button2.visibility = View.VISIBLE
                    }

                    if(mySeatData.reservations[0].confirmed) {
                        binding.txtStatus.visibility = View.VISIBLE
                        binding.txtStatus.text = binding.txtStatus.text.toString() + " " + "확정됨"
                    }
                    else {
                        binding.txtStatus.visibility = View.VISIBLE
                        binding.txtStatus.text = binding.txtStatus.text.toString() + " " + "예약됨"
                    }
                    Log.d("TAG", mySeatData.toString())
                    // 0인덱스 삽입한 것은 수정해야함.
                    binding.txtSeatNumber.text = binding.txtSeatNumber.text.toString() + " " + mySeatData.reservations[0].seat + "번"
                    binding.txtLocation.text = binding.txtLocation.text.toString() + " " + mySeatData.reservations[0].college + " " + mySeatData.reservations[0].roomName
                    var date = Date()
                    date.time = mySeatData.reservations[0].begin
                    var start = simple.format(date)
                    date.time = mySeatData.reservations[0].end
                    var end = simple.format(date)
                    binding.txtTime.text = binding.txtTime.text.toString() + " " + "$start ~ $end"
                    dialog.dismiss()
                }
                // 예약을 하지 않았다면
                else {
                    with(binding) {
                        dialog.dismiss()
                        txtSeatNumber.visibility = View.GONE
                        txtLocation.visibility = View.GONE
                        txtSeat.visibility = View.VISIBLE
                        txtSeat.text = "예약을 진행해주세요."
                        txtTime.visibility = View.GONE
                        button.visibility = View.GONE
                        button2.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<MySeat>, t: Throwable) {
                Log.d("Error", "mySeat 연결 실패 $t")
                dialog.dismiss()
                dismiss()
                toast(requireContext(), "연결실패")
            }

        })
    }

    private fun back() {
        binding.button2.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("반납확인").setMessage("좌석을 반납하시겠습니까?")
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                        val dialog = ProgressDialog(requireContext()).apply {
                            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            show()
                        }
                        var input = HashMap<String, Any>()
                        input["reservationId"] = mySeatData.reservations[0].reservationId
                        input["college"] = MySharedPreferences.getInformation(requireContext()).college
                        input["roomName"] = mySeatData.reservations[0].roomName
                        input["id"] = MySharedPreferences.getUserId(requireContext())
                        input["password"] = MySharedPreferences.getUserPass(requireContext())
                        RetrofitBuilder.api.back(input).enqueue(object : Callback<MySeat> {
                            override fun onResponse(call: Call<MySeat>, response: Response<MySeat>) {
                                Log.d(TAG, response.body()!!.toString())
                                if (response.body()!!.result) {
                                    toast(requireContext(), "반납성공!!")
                                    dismiss()
                                } else {
                                    toast(requireContext(), response.body()!!.response)
                                    dismiss()
                                }
                                dialog.dismiss()
                            }

                            override fun onFailure(call: Call<MySeat>, t: Throwable) {
                                toast(requireContext(), "연결실패!!")

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