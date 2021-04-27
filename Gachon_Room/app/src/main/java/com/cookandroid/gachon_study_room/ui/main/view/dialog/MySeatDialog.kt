package com.cookandroid.gachon_study_room.ui.main.view.dialog

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.MySeat
import com.cookandroid.gachon_study_room.databinding.FragmentMySeatBinding
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.base.BaseBottomSheet
import com.cookandroid.gachon_study_room.ui.main.view.fragment.ReservationFragment.Companion.day
import com.cookandroid.gachon_study_room.ui.main.view.fragment.ReservationFragment.Companion.hour
import com.cookandroid.gachon_study_room.ui.main.view.fragment.ReservationFragment.Companion.minute
import com.cookandroid.gachon_study_room.ui.main.view.fragment.ReservationFragment.Companion.month
import com.cookandroid.gachon_study_room.ui.main.view.fragment.ReservationFragment.Companion.year
import com.cookandroid.gachon_study_room.ui.main.viewmodel.MainViewModel
import com.cookandroid.gachon_study_room.util.Resource
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MySeatDialog : BaseBottomSheet<FragmentMySeatBinding>(R.layout.fragment_my_seat) {
    var mySeatData = MySeat()
    private val dialog by lazy {
        ProgressDialog(requireContext())
    }
    private var TAG = "MYSEAT"
    private val model: MainViewModel by sharedViewModel()
    private var input = HashMap<String, Any>()

    override fun init() {
        super.init()
        cancelSeat()
        extendSeat()
    }

    private fun initViewModel() {
        input["id"] = MySharedPreferences.getUserId(requireContext())
        input["password"] = MySharedPreferences.getUserPass(requireContext())
        model.callMySeat(input)
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer { resource ->
                    when (resource.status) {
                        Resource.Status.SUCCESS -> {
                            dialog.dismiss()
                            when (resource.data!!.result) {
                                true -> {
                                    mySeatData = resource.data!!
                                    model.mySeatData = resource.data!!
                                    mySeat()
                                }
                                false -> {
                                    toast(requireContext(), resource.data!!.response)
                                }
                            }
                        }
                        Resource.Status.ERROR -> {
                            dialog.dismiss()
                            toast(
                                    requireContext(),
                                    resource.message + "\n" + resources.getString(R.string.connect_fail)
                            )
                        }
                        Resource.Status.LOADING -> {
                            dialog.show()
                            with(binding) {
                                txtSeatNumber.visibility = View.GONE
                                txtLocation.visibility = View.GONE
                                txtSeat.visibility = View.GONE
                                txtTime.visibility = View.GONE
                                btnExtend.visibility = View.GONE
                                btnBack.visibility = View.GONE
                                txtStatus.visibility = View.GONE
                            }
                        }
                    }
                })
    }

    private fun mySeat() {
        // 예약을 했다면
        if (mySeatData.reservations.isNotEmpty()) {
            with(binding) {
                txtSeatNumber.visibility = View.VISIBLE
                txtLocation.visibility = View.VISIBLE
                txtSeat.visibility = View.VISIBLE
                txtTime.visibility = View.VISIBLE
                btnExtend.visibility = View.VISIBLE
                btnBack.visibility = View.VISIBLE
            }

            if (mySeatData.reservations[0].confirmed) {
                binding.txtStatus.visibility = View.VISIBLE
                binding.txtStatus.text = "좌석 상태 : 확정됨"
            } else {
                binding.txtStatus.visibility = View.VISIBLE
                binding.txtStatus.text = "좌석 상태 : 예약됨"
            }
            // 0인덱스 삽입한 것은 수정해야함.
            binding.txtSeatNumber.text = "자리 번호 : ${mySeatData.reservations[0].seat}번"
            binding.txtLocation.text = "장소 : ${mySeatData.reservations[0].college} ${mySeatData.reservations[0].roomName}"
            var date = Date()
            date.time = mySeatData.reservations[0].begin
            var start = simple.format(date)
            date.time = mySeatData.reservations[0].end
            var end = simple.format(date)
            binding.txtTime.text = "예약시간 : $start ~ $end"
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
                btnExtend.visibility = View.GONE
                btnBack.visibility = View.GONE
            }
        }
    }

    private fun extendSeat() {
        binding.btnExtend.setOnClickListener {
            when (mySeatData.reservations[0].confirmed) {
                true -> {
                    // 현재시간이  (end+begin)/2 == 3시부터 가능.
                    Log.d(TAG, "$year $month $day $hour $minute")
                    if((mySeatData.reservations[0].begin + mySeatData.reservations[0].end)/2 <= GregorianCalendar(year, month, day, hour, minute).timeInMillis)
                    {
                        ExtensionDialog().show((context as AppCompatActivity).supportFragmentManager, "extend")

                    }
                    else {
                        toast(requireContext(), "연장은 예약시간 반 이상이 지나야 할 수 있습니다.")
                    }
                }
                false -> {
                    toast(requireContext(), "먼저 좌석 확정을 해주세요")
                }
            }
//        findNavController().navigate(MySeatDialogDirections.actionMySeatDialogToExtensionDialog())
        }
    }

    private fun cancelSeat() {
        binding.btnBack.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("반납확인").setMessage("좌석을 반납하시겠습니까?")
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                        input["reservationId"] = mySeatData.reservations[0].reservationId
                        input["college"] = MySharedPreferences.getInformation(requireContext()).college
                        input["roomName"] = mySeatData.reservations[0].roomName //a
                        input["id"] = MySharedPreferences.getUserId(requireContext())
                        input["password"] = MySharedPreferences.getUserPass(requireContext())

                        model.callCancel(input)
                                .observe(viewLifecycleOwner, androidx.lifecycle.Observer { resource ->
                                    when (resource.status) {
                                        Resource.Status.SUCCESS -> {
                                            dialog.dismiss()
                                            when (resource.data!!.result) {
                                                true -> {
                                                    toast(requireContext(), "반납성공!!")

                                                }
                                                false -> {
                                                    toast(requireContext(), resource.data!!.response)
                                                }
                                            }
                                            dismiss()
                                        }
                                        Resource.Status.ERROR -> {
                                            dialog.dismiss()
                                            toast(
                                                    requireContext(),
                                                    resource.message + "\n" + resources.getString(R.string.connect_fail)
                                            )
                                            dismiss()
                                        }
                                        Resource.Status.LOADING -> {
                                            dialog.show()
                                        }
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

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onpause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
        Log.d(TAG, "onResume")
    }

    companion object {
        var simple = SimpleDateFormat("HH시 mm분")
    }
}