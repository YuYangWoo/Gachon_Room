package com.cookandroid.gachon_study_room.ui.main.view.fragment

import android.content.Intent
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.Confirm
import com.cookandroid.gachon_study_room.databinding.FragmentQrBinding
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.ui.main.view.activity.CaptureActivity
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import com.cookandroid.gachon_study_room.ui.main.view.dialog.ProgressDialog
import com.cookandroid.gachon_study_room.ui.main.viewmodel.MainViewModel
import com.cookandroid.gachon_study_room.util.Resource
import com.google.zxing.integration.android.IntentIntegrator
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.HashMap


class QrCodeFragment : BaseFragment<FragmentQrBinding>(R.layout.fragment_qr) {
    private val dialog by lazy {
        ProgressDialog(requireContext())
    }
    private var confirmResult = Confirm()
    private var TAG = "QRCODE"
    private val model: MainViewModel by sharedViewModel()
    var input = HashMap<String, Any>()

    override fun init() {
        super.init()
        scanQRCode()
        btnConfirm()
    }

    // QR Reader
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

    // Stack에서 지우기
    private fun btnConfirm() {
        binding.btnConfirm.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun dataSet() {
        input["roomName"] = MySharedPreferences.getReservation(requireContext()).roomName
        input["college"] = MySharedPreferences.getInformation(requireContext()).college
        input["reservationId"] = MySharedPreferences.getReservation(requireContext()).reservationId
        input["token"] = MySharedPreferences.getToken(requireContext())
        input["id"] = MySharedPreferences.getUserId(requireContext())
        input["password"] = MySharedPreferences.getUserPass(requireContext())
        Log.d(TAG, input.toString())
    }

    // Confirm API 통신
    private fun initViewModel() {
        model.callConfirm(input).observe(viewLifecycleOwner, androidx.lifecycle.Observer { resource ->
            when(resource.status) {
                Resource.Status.SUCCESS -> {
                    confirmResult = resource.data!!
                    when(confirmResult.result) {
                        true -> {
                            toast(requireContext(), "좌석 확정에 성공하였습니다.")
                            binding.txtResult.text = "확정성공"
                        }
                        false -> {
                            toast(requireContext(), confirmResult.response)
                            binding.txtResult.text = "확정실패"
                        }
                    }
                    dialog.dismiss()
                }
                Resource.Status.ERROR -> {
                    toast(requireContext(), resource.message + "\n" + resources.getString(R.string.connect_fail))
                    dialog.dismiss()
                }
                Resource.Status.LOADING -> {
                    dialog.show()
                }
            }
        })
    }

    // QR코드 리드 결과과
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                toast(requireContext(), "Cancelled")
            } else { // 스캔 되었을 때
                Log.d(TAG, result.contents)
                MySharedPreferences.setToken(requireContext(), result.contents)
                dataSet()
                initViewModel()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}