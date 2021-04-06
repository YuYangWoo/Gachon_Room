package com.cookandroid.gachon_study_room.ui.activity

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.Confirm
import com.cookandroid.gachon_study_room.databinding.FragmentQrBinding
import com.cookandroid.gachon_study_room.service.RetrofitBuilder
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.base.BaseActivity
import com.google.zxing.integration.android.IntentIntegrator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class QRCodeActivity : BaseActivity<FragmentQrBinding>(R.layout.fragment_qr) {

    private var TAG = "QRCODE_ACTIVITY"
    override fun init() {
        super.init()
        scanQRCode()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                toast(this, "Cancelled")
            }
            else { // 스캔 되었을 때
                toast(this, "Scanned: " + result.contents)
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
                    override fun onResponse(call: Call<Confirm>, response: Response<Confirm>) {
                       var confirmResult = response.body()!!
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