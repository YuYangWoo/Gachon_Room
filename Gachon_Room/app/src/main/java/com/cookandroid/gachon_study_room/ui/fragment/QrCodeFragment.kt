package com.cookandroid.gachon_study_room.ui.fragment

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.FragmentQrBinding
import com.cookandroid.gachon_study_room.ui.activity.CaptureActivity
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import com.google.zxing.integration.android.IntentIntegrator

class QrCodeFragment : BaseFragment<FragmentQrBinding>(R.layout.fragment_qr) {
    override fun init() {
        super.init()
//        scanQRCode()
        IntentIntegrator(requireContext() as Activity?).initiateScan();

    }

//    private fun scanQRCode() {
//        val integrator = IntentIntegrator(requireContext() as Activity).apply {
//            captureActivity = CaptureActivity::class.java
//            setOrientationLocked(false)
//            setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
//            setPrompt("Scanning Code")
//        }
//        integrator.initiateScan()
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
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
    if (result != null) {
        if (result.contents == null) {
            toast(requireContext(), "Cancelled")
        } else {
            toast(requireContext(), "Scanned: " + result.contents)
        }
    } else {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
}