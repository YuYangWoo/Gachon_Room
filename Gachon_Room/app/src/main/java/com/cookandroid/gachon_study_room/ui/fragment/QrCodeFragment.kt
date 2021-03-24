package com.cookandroid.gachon_study_room.ui.fragment

import android.content.Intent
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.FragmentQrBinding
import com.cookandroid.gachon_study_room.ui.activity.CaptureForm
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import com.google.zxing.integration.android.IntentIntegrator

class QrCodeFragment : BaseFragment<FragmentQrBinding>(R.layout.fragment_qr) {
    private val qrScan by lazy { IntentIntegrator(requireActivity())}
    override fun init() {
        super.init()
        qrScan.setPrompt("QR을 스캔해주세요")
        qrScan.captureActivity = CaptureForm::class.java
        qrScan.setOrientationLocked(false)
        qrScan.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                toast(requireContext(),"Canceled")
            } else {
                toast(requireContext(), "Scanned: " + result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}