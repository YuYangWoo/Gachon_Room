package com.cookandroid.gachon_study_room.ui.main.view.dialog

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import android.widget.TimePicker.OnTimeChangedListener
import androidx.core.content.ContextCompat
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.MySeat
import com.cookandroid.gachon_study_room.data.model.room.Room
import com.cookandroid.gachon_study_room.data.model.room.RoomsData
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.data.singleton.TimeRequest
import com.cookandroid.gachon_study_room.databinding.FragmentExtensionBinding
import com.cookandroid.gachon_study_room.ui.base.BaseDialogFragment
import com.cookandroid.gachon_study_room.ui.main.view.activity.CaptureActivity
import com.cookandroid.gachon_study_room.ui.main.view.fragment.ReservationFragment
import com.cookandroid.gachon_study_room.ui.main.view.fragment.ReservationFragment.Companion.day
import com.cookandroid.gachon_study_room.ui.main.view.fragment.ReservationFragment.Companion.month
import com.cookandroid.gachon_study_room.ui.main.view.fragment.ReservationFragment.Companion.year
import com.cookandroid.gachon_study_room.ui.main.viewmodel.MainViewModel
import com.cookandroid.gachon_study_room.util.Resource
import com.google.zxing.integration.android.IntentIntegrator
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ExtensionDialog : BaseDialogFragment<FragmentExtensionBinding>(R.layout.fragment_extension) {
    private val model: MainViewModel by sharedViewModel()
    private var input = HashMap<String, Any>()
    private val TAG = "EXTENSION"
    private val dialog by lazy {
        ProgressDialog(requireContext())
    }
    private lateinit var timeTable: LinearLayout
    private lateinit var linearTxt: LinearLayout
    private var time = TimeRequest.statusTodayTime() + 1
    private var roomsData = RoomsData()
    private var mySeatData = MySeat()
    private var date = Date()
    private var timeDate = Date()
    var hourr: Int = 0
    var minutee: Int = 0
    private var isPossible = false

    override fun init() {
        super.init()
        mySeatData = model.mySeatData
        txtSet()
        btnClick()
        roomsData()
        setTimePickerInterval(binding.timePicker)
        timePickerClick()
    }

    // 확인버튼 클릭 이벤트
    private fun btnClick() {
        binding.txtConfirm.setOnClickListener {
            if(isPossible) {
                    val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("연장확인")
                    .setMessage("${hourr}시 ${minutee*10}분 까지 연장하시겠습니까?")
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                        // QR스캔
                        scanQRCode()
                    })
                    .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                        Log.d("TAG", "취소")
                    })
            builder.create()
            builder.show()
            }
       else {
           toast(requireContext(), "연장은 최대 2시간 할 수 있습니다.")
            }
        }

        binding.txtCancel.setOnClickListener {
          dismiss()
        }
    }

    // QR Reader화면
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

    // QR로 token을 받고 연장 API통신
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                toast(requireContext(), "Cancelled")
            } else { // 스캔 되었을 때
                Log.d(TAG, result.contents)
                MySharedPreferences.setToken(requireContext(), result.contents)
                initViewModel()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // 현재 종료시간, 연장 종료시간 TextView
    private fun txtSet() {
        date.time = mySeatData.reservations[0].end
        var end = simple.format(date)
        binding.txtCurrent.text =  "기존 종료시간:$end"

        // 타임피커 초기시간
        timeDate.time = mySeatData.reservations[0].end
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            binding.timePicker.hour = hourFormat.format(timeDate).toInt()
            binding.timePicker.minute = (minuteFormat.format(timeDate).toInt())/10 // 10분 인터벌을 줄때 maxValue가 0~5이므로 /10을 해줘야 초기 타임피커 값이 잡힘.
            Log.d(TAG, hourFormat.format(timeDate)+"시"+ minuteFormat.format(timeDate))
            Log.d(TAG, binding.timePicker.minute.toString())
        } else {
            binding.timePicker.currentHour = hourFormat.format(timeDate).toInt()
            binding.timePicker.currentMinute =minuteFormat.format(timeDate).toInt()/10
        }
    }

    // TimePicker 시간이 바뀌었을 때 Listener
    private fun timePickerClick() {
        binding.timePicker.setOnTimeChangedListener(OnTimeChangedListener { timePicker, hour, min ->
            // 연장하는 시간과 기존 시간을 뺀다. >=7200000일 경우 가능하다.
            // 1시까지 연장. 22시40분 7200000보다 커진다. 왜냐하면 2시간 이상 연장 하기 때문에. 그럼 이건 안되야하는것.

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                hourr = binding.timePicker.hour
                minutee = binding.timePicker.minute

                isPossible = if((GregorianCalendar(year,month,day,hourr,minutee*10).timeInMillis) - (mySeatData.reservations[0].end) > 14400000L) {
                    toast(requireContext(), "최대연장 시간은 4시간입니다.")
                    false
                } else {
                    true
                }
                binding.txtEnd.text =  "연장 종료시간:${hourr}시 ${minutee*10}분"

            } else {
                hourr = binding.timePicker.currentHour
                minutee = binding.timePicker.currentMinute
                isPossible = if(GregorianCalendar(year,month,day,hourr,minutee*10).timeInMillis - mySeatData.reservations[0].end > 14400000L) {
                    toast(requireContext(), "최대연장 시간은 4시간입니다.")
                    false
                } else {
                    true
                }
                binding.txtEnd.text ="연장 종료시간:${hourr}시 ${minutee*10}분"

            }
        })
    }

    // RoomsData API 통신
    private fun roomsData() {
        var input = HashMap<String, Any>()
        input["college"] = MySharedPreferences.getInformation(requireContext()).college
        model.callRooms(input).observe(viewLifecycleOwner, androidx.lifecycle.Observer { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    when (resource.data!!.result) {
                        true -> {
                            dialog.dismiss()
                            roomsData = resource.data
                            drawStatusBar(
                                    roomsData.rooms[MySharedPreferences.getRoomPosition(
                                            requireContext()
                                    )].reserved, mySeatData.reservations[0].seat
                            )
                        }
                        false -> {
                            toast(requireContext(), resource.data!!.response)
                        }
                    }
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

    // 좌석 상황바 구현
    private fun drawStatusBar(seatData: List<ArrayList<Room.Reservation>>, position: Int) {
        var statusTime = GregorianCalendar(
                ReservationFragment.year,
                ReservationFragment.month,
                ReservationFragment.day,
                time - 1,
                0
        )
        timeTable = LinearLayout(requireContext())
        timeTable.orientation = LinearLayout.HORIZONTAL
        binding.scrollBar.addView(timeTable)
        var layoutParams = LinearLayout.LayoutParams(30, 50)
        for (i in 1..144) {
            val status = Button(requireContext())
            status.tag = ReservationFragment.AVAILABLE
            status.text = statusTime.timeInMillis.toString()
            status.layoutParams = layoutParams
            timeTable.addView(status)
            for (j in seatData[position].indices) {
                if (status.text.toString().toLong() >= seatData[position][j].begin && status.text.toString().toLong() < seatData[position][j].end) {
                    status.tag = ReservationFragment.UNAVAILABLE
                }

            }
            setBackgroundColor(status)
            // 타임바, 시간 텍스트 구현
            if (i % 6 == 0) {
                if ((time) == 24) { // 오후 23시일 때
                    time = 0
                }
                val timeBar = Button(requireContext())
                setTimeBar(timeBar)
                // 시간 크기
                var txtParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )

                // 시간 textView 설정
                var timeTxt = TextView(requireContext())
                timeTxt.layoutParams = txtParams
                timeTxt.textSize = 10F
                timeTxt.text = time++.toString()

                // 세로 리니어레이아웃을 만들어서 타임바와 텍스트를 넣고 table에 넣어준다.
                linearTxt = LinearLayout(requireContext())
                linearTxt.orientation = LinearLayout.VERTICAL
                linearTxt.addView(timeBar)
                linearTxt.addView(timeTxt)
                linearTxt.gravity = Gravity.CENTER
                timeTable.addView(linearTxt)
                if (time == 24) { // time++로 24시가 될 때 time = 0
                }
            }
            statusTime.timeInMillis += 600000 // 10분
        }
    }

    // 시간 바 색상
    private fun setTimeBar(timeBar: View) {
        // 타임바 크기
        var layoutParams = LinearLayout.LayoutParams(10, 50)
        timeBar.layoutParams = layoutParams
        timeBar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
    }

    // 상황바 색상
    private fun setBackgroundColor(status: View) {
        when (status.tag) {
            ReservationFragment.UNAVAILABLE -> {
                status.setBackgroundResource(R.drawable.status_list_edge)

            }
            ReservationFragment.AVAILABLE -> {
                status.setBackgroundResource(R.drawable.status_list_edge_available)
            }
            else -> {

            }
        }
    }

    // 연장 Request Data Set
    private fun dataSet() {
        input["id"] = MySharedPreferences.getUserId(requireContext())
        input["password"] = MySharedPreferences.getUserPass(requireContext())
        input["college"] = MySharedPreferences.getInformation(requireContext()).college
        input["roomName"] = MySharedPreferences.getReservation(requireContext()).roomName
        input["reservationId"] = MySharedPreferences.getReservation(requireContext()).reservationId
        input["token"] = MySharedPreferences.getToken(requireContext())
        input["extendedTime"] =  GregorianCalendar(
                year,
                month,
                day,
                hourr,
                minutee*10
        ).timeInMillis
    }

    // Extend API 통신
    private fun initViewModel() {
        dataSet()
        model.callExtend(input).observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { resource ->
                    when (resource.status) {
                        Resource.Status.SUCCESS -> {
                            Log.d(TAG, "연결성공" + resource.data.toString())
                            when (resource.data!!.result) {
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

    // TimePicker 10분 간격으로 Set
    private fun setTimePickerInterval(timePicker: TimePicker) {
        try {
            val classForid = Class.forName("com.android.internal.R\$id")
            // Field timePickerField = classForid.getField("timePicker");
            val field: Field = classForid.getField("minute")
            var minutePicker = timePicker.findViewById(field.getInt(null)) as NumberPicker
            minutePicker.minValue = 0
            minutePicker.maxValue = 5
            var displayedValues = ArrayList<String>()
            run {
                var i = 0
                while (i < 60) {
                    displayedValues.add(String.format("%02d", i))
                    i += TIME_PICKER_INTERVAL
                }
            }
            var i = 0
            while (i < 60) {
                displayedValues.add(String.format("%02d", i))
                i += TIME_PICKER_INTERVAL
            }
            minutePicker.displayedValues = displayedValues.toArray(arrayOfNulls<String>(0))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val TIME_PICKER_INTERVAL = 10
        var simple = SimpleDateFormat("HH시 mm분")
        var hourFormat = SimpleDateFormat("HH")
        var minuteFormat = SimpleDateFormat("mm")
    }

}