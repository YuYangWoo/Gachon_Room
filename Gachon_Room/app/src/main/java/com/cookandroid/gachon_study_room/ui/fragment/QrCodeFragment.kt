package com.cookandroid.gachon_study_room.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.Confirm
import com.cookandroid.gachon_study_room.databinding.FragmentQrBinding
import com.cookandroid.gachon_study_room.service.RetrofitBuilder
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.activity.CaptureActivity
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import com.cookandroid.gachon_study_room.ui.dialog.ProgressDialog
import com.google.zxing.integration.android.IntentIntegrator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap


class QrCodeFragment : BaseFragment<FragmentQrBinding>(R.layout.fragment_qr) {

    private var confirmResult: Confirm = Confirm()
    private var TAG = "QRCODE_ACTIVITY"

    override fun init() {
        super.init()
        scanQRCode()
        btnConfirm()
    }

    private fun scanQRCode() {
        val integrator = IntentIntegrator.forSupportFragment(this).apply {
            captureActivity = CaptureActivity::class.java // 가로 세로
            setOrientationLocked(false)
            setPrompt("Scan QR code")
            setBeepEnabled(false) // 소리 on off
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        }

        integrator.initiateScan()
    }

    private fun btnConfirm() {
        binding.btnConfirm.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                toast(requireContext(), "Cancelled")
            } else { // 스캔 되었을 때
                val dialog = ProgressDialog(requireContext()).apply {
                    window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    show()
                }
                binding.txtResult.visibility = View.GONE
                binding.btnConfirm.visibility = View.GONE

                var input = HashMap<String, Any>()
                input["roomName"] = MySharedPreferences.getConfirmRoomName(requireContext())
                input["college"] = MySharedPreferences.getInformation(requireContext()).college
                input["reservationId"] = MySharedPreferences.getConfirmId(requireContext())
                input["token"] = result.contents
                input["id"] = MySharedPreferences.getUserId(requireContext())
                input["password"] = MySharedPreferences.getUserPass(requireContext())
                Log.d(TAG, input.toString())

                RetrofitBuilder.api.confirm(input).enqueue(object : Callback<Confirm> {
                    override fun onResponse(
                            call: Call<Confirm>,
                            response: Response<Confirm>
                    ) {
                        Log.d(TAG, confirmResult.toString())
                        confirmResult = response.body()!!
                        binding.txtResult.visibility = View.VISIBLE
                        binding.btnConfirm.visibility = View.VISIBLE
                        when {
                            confirmResult.result -> {
                                binding.txtResult.text = "확정성공"
                            }
                            else -> {
                                binding.txtResult.text = "확정실패"
                            }
                        }
                        dialog.dismiss()
                        // 확정 실패
//                                if (!confirmResult.result) {
//                                    with(binding) {
//                                        txtSeatNumber.visibility = View.GONE
//                                        txtLocation.visibility = View.GONE
//                                        txtTime.visibility = View.GONE
//                                        txtResult.text = "확정 실패"
//                                        txtStatus.visibility = View.GONE
//                                        btnConfirm.text = confirmResult.response
//                                    }
//                                }
//                                // 확정 성공
//                                else {
//                                    with(binding) {
//                                        txtLocation.text =
//                                            binding.txtLocation.text.toString() + " " + mySeat.reservations[0].college + " " + mySeat.reservations[0].roomName
//                                        txtSeatNumber.text =
//                                            binding.txtSeatNumber.text.toString() + " " + mySeat.reservations[0].seat + "번"
//                                        var date = Date()
//                                        date.time = mySeat.reservations[0].begin
//                                        var start = MySeatDialog.simple.format(date)
//                                        date.time = mySeat.reservations[0].end
//                                        var end = MySeatDialog.simple.format(date)
//                                        txtTime.text =
//                                            binding.txtTime.text.toString() + " " + "$start ~ $end"
//                                        if (mySeat.reservations[0].confirmed) {
//                                            binding.txtStatus.visibility = View.VISIBLE
//                                            binding.txtStatus.text =
//                                                binding.txtStatus.text.toString() + " " + "확정됨"
//                                        } else {
//                                            binding.txtStatus.visibility = View.VISIBLE
//                                            binding.txtStatus.text =
//                                                binding.txtStatus.text.toString() + " " + "예약됨"
//                                        }
//                                    }
//                                }

                        Log.d("TAG", confirmResult.toString())
                    }

                    override fun onFailure(call: Call<Confirm>, t: Throwable) {
                        Log.d("Error", "Error $t")
                    }

                })

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
//        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//        if (result != null) {
//            if (result.contents == null) {
//                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(context, "Scanned : " + result.contents, Toast.LENGTH_LONG).show()
//            }
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//        if (result != null) {
//            if (result.contents == null) toast(requireContext(), "Cancelled")
//            else toast(requireContext(), "Scanned: " + result.contents)
//        } else {
//            super.onActivityResult(requestCode, resultCode, data)
//        }
//    }

}