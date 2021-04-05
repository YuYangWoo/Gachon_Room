package com.cookandroid.gachon_study_room.ui.activity

import android.content.Intent
import android.widget.Toast
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.FragmentQrBinding
import com.cookandroid.gachon_study_room.ui.base.BaseActivity
import com.google.zxing.integration.android.IntentIntegrator

class QRCodeActivity : BaseActivity<FragmentQrBinding>(R.layout.fragment_qr) {
    override fun init() {
        super.init()
        IntentIntegrator(this).initiateScan();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}