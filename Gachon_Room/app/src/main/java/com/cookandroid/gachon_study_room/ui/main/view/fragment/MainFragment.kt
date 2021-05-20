package com.cookandroid.gachon_study_room.ui.main.view.fragment

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.Confirm
import com.cookandroid.gachon_study_room.data.model.Information
import com.cookandroid.gachon_study_room.databinding.FragmentMainBinding
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.data.singleton.TimeRequest
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import com.cookandroid.gachon_study_room.ui.main.view.activity.CaptureActivity
import com.cookandroid.gachon_study_room.ui.main.view.dialog.ProgressDialog
import com.cookandroid.gachon_study_room.ui.main.viewmodel.MainViewModel
import com.cookandroid.gachon_study_room.util.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import org.koin.android.viewmodel.compat.SharedViewModelCompat.sharedViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.HashMap

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val TAG = "MAIN"
    private val info: Information.Account by lazy {
        MySharedPreferences.getInformation(requireContext())
    }
    private val dialog by lazy {
        ProgressDialog(requireContext())
    }
    private val model: MainViewModel by sharedViewModel()
    private var input = HashMap<String, Any>()
    private var confirmResult = Confirm()

    override fun init() {
        super.init()
        btnOption()
        btnClick()
        binding.student = info

    }

    private fun btnOption() {
        binding.btnOption.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToOptionFragment())
        }
    }

    private fun btnClick() {
        binding.btnMySeat.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToMySeatDialog())
        }

        binding.btnReservation.setOnClickListener {
            RoomListFragment().show((context as AppCompatActivity).supportFragmentManager,"Modal")
        }

        binding.btnConfirm.setOnClickListener {
          scanQRCode()
        }
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

    // Confirm API 통신
    private fun initViewModel() {
        model.callConfirm(input).observe(viewLifecycleOwner, androidx.lifecycle.Observer { resource ->
            when(resource.status) {
                Resource.Status.SUCCESS -> {
                    confirmResult = resource.data!!
                    Log.d(TAG, confirmResult.toString())
                    when(confirmResult.result) {
                        true -> {
                            snackBar(resources.getString(R.string.confirm_message))
                        }
                        false -> {
                            if(confirmResult.response == "INVALID_TOKEN") {
                                snackBar(resources.getString(R.string.qr_refresh))
                            }
                            else {
                                snackBar(confirmResult.response)
                            }
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

    private fun dataSet() {
        input["roomName"] = MySharedPreferences.getReservation(requireContext()).roomName
        input["college"] = MySharedPreferences.getInformation(requireContext()).college
        input["reservationId"] = MySharedPreferences.getReservation(requireContext()).reservationId
        input["token"] = MySharedPreferences.getToken(requireContext())
        input["id"] = MySharedPreferences.getUserId(requireContext())
        input["password"] = MySharedPreferences.getUserPass(requireContext())
        Log.d(TAG, input.toString())
    }

}