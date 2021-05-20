package com.cookandroid.gachon_study_room.ui.main.view.fragment

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.text.format.DateFormat
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.Confirm
import com.cookandroid.gachon_study_room.data.model.Reserve
import com.cookandroid.gachon_study_room.data.model.room.Room
import com.cookandroid.gachon_study_room.data.model.room.RoomsData
import com.cookandroid.gachon_study_room.data.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.data.singleton.TimeRequest
import com.cookandroid.gachon_study_room.databinding.FragmentReservationBinding
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import com.cookandroid.gachon_study_room.ui.main.view.dialog.ConfirmDialog
import com.cookandroid.gachon_study_room.ui.main.view.dialog.CustomTimePickerDialog
import com.cookandroid.gachon_study_room.ui.main.view.dialog.ProgressDialog
import com.cookandroid.gachon_study_room.ui.main.view.dialog.TimePickerDialogFixedNougatSpinner
import com.cookandroid.gachon_study_room.ui.main.viewmodel.MainViewModel
import com.cookandroid.gachon_study_room.util.Resource
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ReservationFragment :
        BaseFragment<FragmentReservationBinding>(R.layout.fragment_reservation) {
    private val args: ReservationFragmentArgs by navArgs()
    private lateinit var layout: ViewGroup
    private var seatViewList = ArrayList<TextView>()
    private var seatSize = 80
    private var rooms: RoomsData = RoomsData()
    private lateinit var name: String
    private lateinit var table: LinearLayout
    private lateinit var timeTable: LinearLayout
    private lateinit var linearTxt: LinearLayout
    var selectedIds = ""
    private val TAG = "RESERVATION"
    private var startTime = 0L // 실제 사용되는 long값인 시작 값
    private var endTime = 0L // 실제 사용되는 long값인 끝나는 시간 값
    private var check = -1
    private var startOurHour = 0 // TimePickerDialog의 초기 값을 설정하는 시간
    private var startOurMinute = 0 // TimePickerDialog의 초기 값을 설정하는 분
    private var endOurHour = 0 // TimePickerDialog의 초기 값을 설정하는 시간
    private var endOurMinute = 0 // TimePickerDialog의 초기 값을 설정하는 분
    private var seatId = 0
    private var txtStartTime = "" // Text 시간을 나타내는 시작 시간
    private var txtEndTime = "" // Text 시간을 나타내는 끝나는 시간
    private val model: MainViewModel by sharedViewModel()
    private val dialog by lazy {
        ProgressDialog(requireContext())
    }
    var input = HashMap<String, Any>()
    private var reservation: Reserve = Reserve()
    private var seatTime = ArrayList<ArrayList<Room.Reservation>>()
    private var time = TimeRequest.statusTodayTime() + 1
    private var selectedSeat: Int = 0

    override fun init() {
        super.init()
        ConfirmDialog(requireContext()).show()

        layout = binding.layoutSeat
        rooms = args.rooms
        name = args.name
        startTime = TimeRequest.timeLong().time
        endTime = TimeRequest.endTimeLong().time
        Log.d(TAG, selectedSeat.toString())
        // RoomListFragment 리스트의 이름과 방의 이름과 일치하면 좌석 그려주기
        for (i in 0 until rooms.rooms.size) {
            if (name == rooms.rooms[i].roomName) {
                drawSeatView(rooms.rooms[i].seat, rooms, i)
                Log.d(TAG, "방의 pos는 $i 입니다.")
            }
        }
        timeSet()
        btnStart()
        btnEnd()
        btnConfirm()
    }

    // 시작시간 버튼 클릭 이벤트
    private fun btnStart() {
        binding.txtStart.text = TimeRequest.time()
        binding.cardViewStart.setOnClickListener {
            // 아래 hour, minute가 선택된 시간 분
            var timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker,
                                                                       hour, minute ->
                var startMinute = minute
                Log.d(TAG, "$hour   $minute")
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, startMinute)
                // 좌석 시간 표시
                txtStartTime = simple.format(cal.time)
                var time = GregorianCalendar(year, month, day, hour, startMinute)
                startTime = time.timeInMillis
                // 심플타임으로 나타냄
                var date = Date()
                date.time = time.timeInMillis
                Log.d(TAG, "시작시간은$startTime 심플타임 ${simple.format(date)}")
                // RoomListFragment 리스트의 이름과 방의 이름과 일치하면 좌석 그려주기
                when {
                    startTime < TimeRequest.todayTime() -> { // 시작시간이 현재 시간보다 작을 때
                        toast(requireContext(), resources.getString(R.string.time_error01))
                    }
                    startTime >= endTime -> { // 시작시간이 종료시간 보다 클 때
                        // 끝나는 시간 계산
                        cal.set(Calendar.HOUR_OF_DAY, hour + 4)
                        txtEndTime = simple.format(cal.time)
                        endOurHour = hour + 4
                        if(endOurHour >= 24) { // 시간이 24보다 커버리면
                            endOurHour -= 24
                            var time = GregorianCalendar(year, month, day + 1, hour + 4, startMinute)
                            endTime = time.timeInMillis
                        }
                        else {
                            var time = GregorianCalendar(year, month, day, hour + 4, startMinute)
                            endTime = time.timeInMillis
                        }
                        endOurMinute = startMinute
                        binding.txtEnd.text = txtEndTime

                        // 시작 시간 계산
                        startOurHour = hour
                        startOurMinute = startMinute
                        binding.txtStart.text = txtStartTime
                        for (i in 0 until rooms.rooms.size) {
                            if (name == rooms.rooms[i].roomName) {
                                drawSeatView(rooms.rooms[i].seat, rooms, i)
                            }
                        }
                    }
                    startTime < endTime -> {
                        for (i in 0 until rooms.rooms.size) {
                            if (name == rooms.rooms[i].roomName) {
                                drawSeatView(rooms.rooms[i].seat, rooms, i)
                            }
                        }
                        // ok 버튼 누르고 나오는 시간.
                        startOurHour = hour
                        startOurMinute = startMinute
                        binding.txtStart.text = txtStartTime
                    }
                }
            }
            // 이부분 초기 설정 값으로 넣어주기. 아래 hour minute가 다이얼로그 나타날때 뜨는 시간
            if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.N) {
                TimePickerDialogFixedNougatSpinner(
                        requireContext(),
                        timeSetListener,
                        startOurHour,
                        startOurMinute,
                        DateFormat.is24HourFormat(requireContext())
                ).show()
            } else {
                CustomTimePickerDialog(
                        requireContext(),
                        timeSetListener,
                        startOurHour,
                        startOurMinute,
                        DateFormat.is24HourFormat(requireContext())
                ).show()
            }
        }

    }

    // 종료시간 버튼 클릭
    private fun btnEnd() {
        binding.txtEnd.text = TimeRequest.endTime()
        Log.d(TAG, "끝나는 시간은 $endOurHour $endOurMinute")
        binding.cardViewEnd.setOnClickListener {
            Log.d(TAG, "끝나는 시간은 $endOurHour $endOurMinute")

            var timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker,
                                                                       hour, minute ->
                Log.d(TAG, "리스너시작")
                var endMinute = minute
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, endMinute)
                var time = GregorianCalendar(year, month, day, hour, endMinute)
                txtEndTime = simple.format(cal.time)
                endTime = time.timeInMillis
                var date = Date()
                date.time = time.timeInMillis
                Log.d("TAG", "종료시간은$endTime 심플타임 ${simple.format(date)}")
                // RoomListFragment 리스트의 이름과 방의 이름과 일치하면 좌석 그려주기

                when {
                    endTime < TimeRequest.todayTime() -> {
                        toast(requireContext(), resources.getString(R.string.time_error02))
                    }
                    startTime > endTime -> {
                        toast(requireContext(), resources.getString(R.string.time_error03))
                    }
                    startTime < endTime -> {
                        for (i in 0 until rooms.rooms.size) {
                            if (name == rooms.rooms[i].roomName) {
                                drawSeatView(rooms.rooms[i].seat, rooms, i)
                            }
                        }
                        endOurHour = hour
                        endOurMinute = endMinute
                        binding.txtEnd.text = txtEndTime
                    }
                }
            }

            if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.N) {
                TimePickerDialogFixedNougatSpinner(
                        requireContext(),
                        timeSetListener,
                        endOurHour,
                        endOurMinute,
                        DateFormat.is24HourFormat(requireContext())
                ).show()
            } else {
                CustomTimePickerDialog(
                        requireContext(),
                        timeSetListener,
                        endOurHour,
                        endOurMinute,
                        DateFormat.is24HourFormat(requireContext())
                ).show()
            }
        }

    }

    // 정각에 대응하는 timeSet과 년,월,일,요일 Set
    private fun timeSet() {
        var today = Date()
        var dateFormet = SimpleDateFormat("yyyy-MM-dd E")
        binding.txtCurrentTime.text = dateFormet.format(today)

        var calc = Calendar.getInstance()
        Log.d(TAG, "캘린더 분은 ${calc.get(Calendar.MINUTE)} interval ${interval}")
        if (calc.get(Calendar.MINUTE) + interval == 60) { // 여기는 시간 조절할 때 else 숫자보다 1이 커야함 ex) 4시간 뒤를 보여줄거다 if는 5 else는 4
            startOurHour = calc.get(Calendar.HOUR_OF_DAY) + 1
            endOurHour = calc.get(Calendar.HOUR_OF_DAY) + 5
            startOurMinute = 0
            endOurMinute = 0
        } else { // 종료시간 버튼 눌렀을 때의 시간 control
            startOurHour = calc.get(Calendar.HOUR_OF_DAY)
            startOurMinute = calc.get(Calendar.MINUTE) + interval
            endOurHour = calc.get(Calendar.HOUR_OF_DAY) + 4
            endOurMinute = calc.get(Calendar.MINUTE) + interval
        }

    }

    // 좌석 그려주기
    private fun drawSeatView(room: ArrayList<Array<Int>>, roomData: RoomsData, index: Int) {
        val layoutSeat = LinearLayout(requireContext())
        val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutSeat.removeAllViews()
        layout.removeAllViews()
        layoutSeat.orientation = LinearLayout.VERTICAL
        layoutSeat.layoutParams = params
        layoutSeat.setPadding(seatGaping, seatGaping, seatGaping, seatGaping)
        layoutSeat.gravity = Gravity.CENTER
        layout.addView(layoutSeat)
        seatTime = rooms.rooms[index].reserved as ArrayList<ArrayList<Room.Reservation>>
        var seats = room

        for (i in seats.indices) {
            for (j in seats[i].indices) {
                if (j == START || j == seats.size + 1) {
                    table = LinearLayout(requireContext())
                    table.orientation = LinearLayout.HORIZONTAL
                    layoutSeat.addView(table)
                } else if (seats[i][j] == WALL) {
                    val view = TextView(requireContext())
                    var layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                    view.layoutParams = layoutParams
                    view.setBackgroundColor(Color.TRANSPARENT)
                    table.addView(view)
                } else if (seats[i][j] == EMPTY) {
                    val view = TextView(requireContext())
                    var layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                    view.layoutParams = layoutParams
                    view.setBackgroundColor(Color.TRANSPARENT)
                    table.addView(view)
                } else if (seats[i][j] == DOOR) {
                    val view = TextView(requireContext())
                    var layoutParams: LinearLayout.LayoutParams =
                            LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)

                    view.layoutParams = layoutParams
                    view.id = count
                    view.setBackgroundResource(R.drawable.door)

                    table.addView(view)
                    seatViewList.add(view)
                } else {
                    val view = TextView(requireContext())
                    var layoutParams: LinearLayout.LayoutParams =
                            LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)

                    // 좌석의 번호에 따른 reserved 크기를 구해 예약된 시간과 비교
                    // roomData.rooms[index]까지가 2층, 스터디룸 등
                    // reserved[seats[i][j]]는 seat 번호
                    for (z in 0 until roomData.rooms[index].reserved[seats[i][j]].size) {
                        // 예약하려는 시작시간이 예약된 종료시간보다 작고 예약하려는 종료시간이 예약된 시간 시작시간보다 크면 reserved confirmed면 booked
                        if (startTime < roomData.rooms[index].reserved[seats[i][j]][z].end && endTime > roomData.rooms[index].reserved[seats[i][j]][z].begin) {
                            check = STATUS_RESERVED
                            if (roomData.rooms[index].reserved[seats[i][j]][z].confirmed) {
                                check = STATUS_BOOKED
                            }
                        }
                    }
                    when (check) {
                        STATUS_RESERVED -> {
                            view.setBackgroundResource(R.drawable.ic_seats_reserved)
                            view.tag = STATUS_RESERVED
                        }
                        STATUS_BOOKED -> {
                            view.setBackgroundResource(R.drawable.ic_seats_booked)
                            view.tag = STATUS_BOOKED
                        }
                        else -> {
                            view.setBackgroundResource(R.drawable.ic_seats_book)
                            view.tag = STATUS_AVAILABLE
                        }
                    }

                    view.layoutParams = layoutParams
                    view.setPadding(0, 0, 0, 2 * seatGaping)
                    view.id = seats[i][j]
                    view.gravity = Gravity.CENTER
                    view.setTextColor(Color.BLACK)
                    view.text = seats[i][j].toString()
                    view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                    // 만약 선택됬었던 경우
                    if (view.id == selectedSeat) {
                        view.setBackgroundResource(R.drawable.ic_seats_selected)
                    }
                    table.addView(view)
                    seatViewList.add(view)
                    view.setOnClickListener { seatClick(view) }
                    check = -1
                }

            }
        }

    }

    // 좌석 클릭 이벤트
    private fun seatClick(view: View) {
        if (view.tag as Int == STATUS_AVAILABLE) {
            // 다시 눌렀을 시
            if (selectedIds.contains(view.id.toString() + ",")) {
                selectedIds = selectedIds.replace(view.id.toString() + ",", "")
                view.setBackgroundResource(R.drawable.ic_seats_book)
                Log.d("TAG", selectedIds)
                binding.scrollBar.removeAllViews()
                binding.scrollBar.visibility = View.GONE // 상황바 없애기
                // 하나만 선택가능
            } else { // 좌석 선택했을시
                if (selectedIds == "") { // 한개 선택했을 시
                    // 방이름과 매치한 후에 상황바 그려준다. 상황바 보이기
                    binding.scrollBar.visibility = View.VISIBLE
                    for (i in 0 until rooms.rooms.size) {
                        if (name == rooms.rooms[i].roomName) {
                            drawStatusBar(rooms.rooms[i].reserved, view.id)
                        }
                    }
                    selectedIds = selectedIds + view.id + ","
                    seatId = view.id
                    view.setBackgroundResource(R.drawable.ic_seats_selected)
                    selectedSeat = view.id
                } else { // 2개이상 선택했을 시
                    toast(requireContext(), resources.getString(R.string.seat_error01))
                }

            }
        } else if (view.tag as Int == STATUS_BOOKED) {
            reservedSeatStatusBar(view)
//            toast(requireContext(), "Seat " + view.id + " is Booked")

        } else if (view.tag as Int == STATUS_RESERVED) {
            reservedSeatStatusBar(view)
//            toast(requireContext(), "Seat " + view.id + " is Reserved")
        }
    }

    // 예약된 좌석 클릭했을 때 이벤트
    private fun reservedSeatStatusBar(view: View) {
        if (selectedIds.contains(view.id.toString() + ",")) {
            selectedIds = selectedIds.replace(view.id.toString() + ",", "")
            Log.d("TAG", selectedIds)
            binding.scrollBar.removeAllViews()
            binding.scrollBar.visibility = View.GONE // 상황바 없애기
            // 하나만 선택가능
        } else { // 좌석 선택했을시
            if (selectedIds == "") { // 한개 선택했을 시
                // 방이름과 매치한 후에 상황바 그려준다. 상황바 보이기
                binding.scrollBar.visibility = View.VISIBLE
                for (i in 0 until rooms.rooms.size) {
                    if (name == rooms.rooms[i].roomName) {
                        drawStatusBar(rooms.rooms[i].reserved, view.id)
                    }
                }
                selectedIds = selectedIds + view.id + ","
            } else { // 2개이상 선택했을 시
                toast(requireContext(), resources.getString(R.string.seat_error01))
            }

        }
    }

    // 10분이 1800000
    private fun drawStatusBar(seatData: List<ArrayList<Room.Reservation>>, position: Int) {
        var statusTime = GregorianCalendar(year, month, day, time - 1, 0)
        timeTable = LinearLayout(requireContext())
        timeTable.orientation = LinearLayout.HORIZONTAL
        binding.scrollBar.addView(timeTable)
        var layoutParams = LinearLayout.LayoutParams(30, 50)
        for (i in 1..144) {
            val status = Button(requireContext())
            status.tag = AVAILABLE
            status.text = statusTime.timeInMillis.toString()
            status.layoutParams = layoutParams
            timeTable.addView(status)
            for (j in seatData[position].indices) {
                if (status.text.toString().toLong() >= seatData[position][j].begin && status.text.toString().toLong() < seatData[position][j].end) {
                    status.tag = UNAVAILABLE
                }

            }
            setBackgroundColor(status)
            // 타임바, 시간 텍스트 구현
            if (i % 6 == 0) {
                 if((time) == 24) {
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
                if (time == 24) {
                    time = 0
                }
            }
            statusTime.timeInMillis += 600000
        }
    }

    // 시간바 색상 Set
    private fun setTimeBar(timeBar: View) {
        //타임바 크기
        var layoutParams = LinearLayout.LayoutParams(10, 50)
        timeBar.layoutParams = layoutParams
        timeBar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
    }

    // 상황바 색상 Set
    private fun setBackgroundColor(status: View) {
        when (status.tag) {
            UNAVAILABLE -> {
                status.setBackgroundResource(R.drawable.status_list_edge)

            }
            AVAILABLE -> {
                status.setBackgroundResource(R.drawable.status_list_edge_available)
            }
            else -> {

            }
        }
    }

    // 예약 확인 버튼 클릭 이벤트
    private fun btnConfirm() {
        binding.btnConfirm.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            var date = Date()
            var endDate = Date()
            date.time = startTime
            endDate.time = endTime
            if (seatId == 0) {
                toast(requireContext(), resources.getString(R.string.seat_confirm))
            } else {
                builder.setTitle(resources.getString(R.string.reserve_message))
                        .setMessage("${resources.getString(R.string.seat_time)} ${simple.format(date)} ~ ${simple.format(endDate)}\n${resources.getString(R.string.seat_number)} $seatId ${resources.getString(R.string.reserve_message01)}")
                        .setPositiveButton(resources.getString(R.string.confirm), DialogInterface.OnClickListener { dialogInterface, i ->
                            initViewModel()
                        })
                        .setNegativeButton(resources.getString(R.string.cancel), DialogInterface.OnClickListener { dialogInterface, i ->
                            Log.d("TAG", "취소")
                        })
                builder.create()
                builder.show()
            }
        }
    }

    private fun dataSet() {
        input["studentId"] =
                MySharedPreferences.getInformation(requireContext()).studentId
        input["college"] =
                MySharedPreferences.getInformation(requireContext()).college
        input["roomName"] = name
        input["seat"] = seatId
        input["begin"] = startTime
        input["end"] = endTime
        input["id"] = MySharedPreferences.getUserId(requireContext())
        input["password"] = MySharedPreferences.getUserPass(requireContext())
    }

    // 예약 API 통신
    private fun initViewModel() {
        dataSet()
        model.callReserve(input)
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer { resource ->
                    when (resource.status) {
                        Resource.Status.SUCCESS -> {
                            reservation = resource.data!!
                            Log.d(TAG, reservation.toString())
                            when (reservation.result) {
                                true -> {
                                    MySharedPreferences.setReservation(
                                            requireContext(),
                                            reservation.reservation
                                    )
                                    for (i in 0 until rooms.rooms.size) {
                                        if (name == rooms.rooms[i].roomName) {
                                            MySharedPreferences.setRoomPosition(requireContext(), i)
                                            Log.d(TAG, "방의 pos는 $i 입니다.")
                                        }
                                    }
                                    toast(requireContext(), resources.getString(R.string.reserve_message02))
                                }
                                false -> {
                                    toast(requireContext(), reservation.response)
                                }
                            }
                            dialog.dismiss()
                            findNavController().popBackStack()
                        }
                        Resource.Status.ERROR -> {
                            toast(
                                    requireContext(),
                                    resource.message + "\n" + resources.getString(R.string.connect_fail)
                            )
                            dialog.dismiss()
                        }
                        Resource.Status.LOADING -> {
                            dialog.show()
                        }
                    }
                })
    }

    companion object {
        const val EMPTY = 0
        const val START = 0
        const val DOOR = -2
        const val WALL = -1
        const val STATUS_AVAILABLE = 1
        const val STATUS_BOOKED = 2
        const val STATUS_RESERVED = 3
        const val UNAVAILABLE = 0
        const val AVAILABLE = 1
        const val TIME_BAR = 2

        const val seatGaping = 10
        const val count = 0
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val interval = 10 - (cal.get(Calendar.MINUTE) % 10)
        val simple = SimpleDateFormat("HH시 mm분")
    }

}


