package com.cookandroid.gachon_study_room.ui.main.view.dialog

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.databinding.FragmentExtensionBinding
import com.cookandroid.gachon_study_room.ui.base.BaseDialogFragment
import com.cookandroid.gachon_study_room.ui.main.view.fragment.ReservationFragment
import com.cookandroid.gachon_study_room.ui.main.viewmodel.MainViewModel
import com.cookandroid.gachon_study_room.util.Resource
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.Observer

class ExtensionDialog : BaseDialogFragment<FragmentExtensionBinding>(R.layout.fragment_extension) {

    private val builder by lazy { AlertDialog.Builder(requireContext()) }
    private val model: MainViewModel by sharedViewModel()
    private var input = HashMap<String, Any>()
    private val TAG = "EXTENSION"
    private val dialog by lazy {
        ProgressDialog(requireContext())
    }
    override fun init() {
        super.init()
        btnClick()
    }

    private fun btnClick() {
        binding.btn30.setOnClickListener {
            dataSet(1800000L)
            builder.setTitle("연장")
                    .setMessage("${binding.btn30.text.toString()} 연장하시겠습니까?")
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                        initViewModel()
                    })
                    .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                        Log.d("TAG", "취소")
                    })
            builder.create()
            builder.show()
        }
        binding.btn60.setOnClickListener {
            dataSet(3600000L)

            builder.setTitle("연장")
                    .setMessage("${binding.btn60.text.toString()} 연장하시겠습니까?")
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                        initViewModel()

                    })
                    .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                        Log.d("TAG", "취소")
                    })
            builder.create()
            builder.show()
        }
        binding.btn90.setOnClickListener {
            dataSet(5400000L)

            builder.setTitle("연장")
                    .setMessage("${binding.btn90.text.toString()} 연장하시겠습니까?")
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                        initViewModel()

                    })
                    .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                        Log.d("TAG", "취소")
                    })
            builder.create()
            builder.show()
        }
        binding.btn120.setOnClickListener {
            dataSet(7200000L)

            builder.setTitle("연장")
                    .setMessage("${binding.btn120.text} 연장하시겠습니까?")
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                        initViewModel()

                    })
                    .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                        Log.d("TAG", "취소")
                    })
            builder.create()
            builder.show()
        }

    }

    private fun dataSet(extendedTime: Long) {
        input["id"] = MySharedPreferences.getUserId(requireContext())
        input["password"] = MySharedPreferences.getUserPass(requireContext())
        input["college"] = MySharedPreferences.getInformation(requireContext()).college
        input["roomName"] = MySharedPreferences.getReservation(requireContext()).roomName
        input["reservationId"] = MySharedPreferences.getReservation(requireContext()).reservationId
        input["token"] = MySharedPreferences.getToken(requireContext())
        input["extendedTime"] = MySharedPreferences.getReservation(requireContext()).end + extendedTime
    }

    private fun initViewModel() {
        model.callExtend(input).observe(viewLifecycleOwner, androidx.lifecycle.Observer { resource ->
            when(resource.status) {
                Resource.Status.SUCCESS -> {
                    Log.d(TAG, "연결성공" + resource.data.toString())
                    when(resource.data!!.result) {
                        true -> {
                            toast(requireContext(), "연장성공")
                        }
                        false -> {
                            toast(requireContext(), resource.data!!.response)
                        }
                    }
                    dialog.dismiss()
                    dismiss()
                }
                Resource.Status.LOADING -> {
                    dialog.show()
                }
                Resource.Status.ERROR -> {
                    dialog.dismiss()
                    toast(
                            requireContext(),
                            resource.message + "\n" + resources.getString(R.string.connect_fail)
                    )
                }
            }
        })
    }
}