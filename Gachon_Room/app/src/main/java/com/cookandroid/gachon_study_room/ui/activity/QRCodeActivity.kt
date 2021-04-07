package com.cookandroid.gachon_study_room.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.Confirm
import com.cookandroid.gachon_study_room.data.MySeat
import com.cookandroid.gachon_study_room.databinding.FragmentQrBinding
import com.cookandroid.gachon_study_room.service.RetrofitBuilder
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.base.BaseActivity
import com.cookandroid.gachon_study_room.ui.dialog.MySeatDialog
import com.cookandroid.gachon_study_room.ui.dialog.ProgressDialog
import com.cookandroid.gachon_study_room.ui.fragment.MainFragmentDirections
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class QRCodeActivity : BaseActivity<FragmentQrBinding>(R.layout.fragment_qr) {

    private var TAG = "QRCODE_ACTIVITY"
    private var confirmResult: Confirm = Confirm()
    private var mySeat: MySeat = MySeat()
    override fun init() {
        super.init()
        scanQRCode()
        btnConfirm()
    }

    private fun scanQRCode() {
        val integrator = IntentIntegrator(this).apply {
            captureActivity = CaptureActivity::class.java
            setOrientationLocked(false)
            setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            setPrompt("Scanning Code")
        }
        integrator.initiateScan()
    }

    private fun btnConfirm() {
        binding.btnConfirm.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                toast(this, "Cancelled")
            } else { // 스캔 되었을 때
                val dialog = ProgressDialog(this@QRCodeActivity).apply {
                    window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    show()
                }
                binding.txtResult.visibility = View.GONE
                binding.btnConfirm.visibility = View.GONE
                Log.d(TAG, result.contents)
                var input = HashMap<String, Any>()
                input["roomName"] = MySharedPreferences.getConfirmRoomName(this)
                input["college"] = MySharedPreferences.getInformation(this).college
                input["reservationId"] = MySharedPreferences.getConfirmId(this)
                input["token"] = result.contents
                input["id"] = MySharedPreferences.getUserId(this)
                input["password"] = MySharedPreferences.getUserPass(this)
                Log.d(TAG, input.toString())

                RetrofitBuilder.api.confirm(input).enqueue(object : Callback<Confirm> {
                            override fun onResponse(
                                call: Call<Confirm>,
                                response: Response<Confirm>
                            ) {
                                confirmResult = response.body()!!
                                binding.txtResult.visibility = View.VISIBLE
                                binding.btnConfirm.visibility = View.VISIBLE
                                if(confirmResult.result)  {
                                    binding.txtResult.text = "확정성공"
                                }
                                else {
                                    binding.txtResult.text = "확정실패"
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

}