package com.cookandroid.gachon_study_room.ui.fragment

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.format.DateFormat
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.adapter.AvailiableAdapter
import com.cookandroid.gachon_study_room.data.MySeat
import com.cookandroid.gachon_study_room.data.Reserve
import com.cookandroid.gachon_study_room.data.room.Availiable
import com.cookandroid.gachon_study_room.data.room.RoomsData
import com.cookandroid.gachon_study_room.databinding.FragmentReservationBinding
import com.cookandroid.gachon_study_room.service.RetrofitBuilder
import com.cookandroid.gachon_study_room.singleton.MySharedPreferences
import com.cookandroid.gachon_study_room.singleton.TimeRequest
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import com.cookandroid.gachon_study_room.ui.dialog.CustomTimePickerDialog
import com.cookandroid.gachon_study_room.ui.dialog.ProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ReservationFragment :
    BaseFragment<FragmentReservationBinding>(R.layout.fragment_reservation) {
    private val args: ReservationFragmentArgs by navArgs()
    private lateinit var layout: ViewGroup
    private var seatViewList = ArrayList<TextView>()
    private var seatSize = 100
    private lateinit var rooms: RoomsData
    private lateinit var name: String
    private lateinit var table: LinearLayout
    var selectedIds = ""
    private val TAG = "RESERVATION"
    private var startTime = 0L
    private var endTime = 0L
    private var check = -1
    private var startOurHour = 0
    private var startOurMinute = 0
    private var endOurHour = 0
    private var endOurMinute = 0
    private var seatId = 0
    private var txtStartTime = ""
    private var txtEndTime = ""

    override fun init() {
        super.init()
        layout = binding.layoutSeat
        rooms = args.rooms
        name = args.name

        startTime = TimeRequest.timeLong().time
        endTime = TimeRequest.endTimeLong().time

        // RoomListFragment 리스트의 이름과 방의 이름과 일치하면 좌석 그려주기
        for (i in 0 until rooms.rooms.size) {
            if (name == rooms.rooms[i].roomName) {
                seatView(rooms.rooms[i].seat, rooms, i)
            }
        }
        setAvailableRecyclerView()
        timeSet()
        btnStart()
        btnEnd()
        btnConfirm()
    }

    private fun btnStart() {
        binding.txtStart.text = TimeRequest.time()
        binding.cardViewStart.setOnClickListener {
            // 아래 hour, minute가 선택된 시간 분
            var timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker,
                                                                       hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                txtStartTime = simple.format(cal.time)
                var time = GregorianCalendar(year, month, day, hour, minute)
                startTime = time.timeInMillis
                var date = Date()
                date.time = time.timeInMillis
                Log.d("TAG", "시작시간은$startTime 심플타임 ${simple.format(date)}")
                // RoomListFragment 리스트의 이름과 방의 이름과 일치하면 좌석 그려주기
                when {

                    startTime < TimeRequest.todayTime() -> {
                        toast(requireContext(), "대여 시각은 현재 시각 이후부터 설정할 수 있습니다.")
                    }

                    startTime > endTime -> {
                        toast(requireContext(), "대여 시각은 종료 시각보다 클 수 없습니다.")
                    }

                    startTime < endTime -> {
                        for (i in 0 until rooms.rooms.size) {
                            if (name == rooms.rooms[i].roomName) {
                                seatView(rooms.rooms[i].seat, rooms, i)
                            }
                        }
                        // ok 버트 누르고 나오는 시간.
                        startOurHour = hour
                        startOurMinute = minute
                        binding.txtStart.text = txtStartTime
                    }

                }

            }
            // 이부분 초기 설정 값으로 넣어주기. 아래 hour minute가 다이얼로그 나타날때 뜨는 시간
            CustomTimePickerDialog(
                requireContext(),
                timeSetListener,
                startOurHour,
                startOurMinute,
                DateFormat.is24HourFormat(requireContext())
            ).show()
        }
    }


    private fun btnEnd() {
        binding.txtEnd.text = TimeRequest.endTime()
        binding.cardViewEnd.setOnClickListener {
            var timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker,
                                                                       endHour, endMinute ->
                cal.set(Calendar.HOUR_OF_DAY, endHour)
                cal.set(Calendar.MINUTE, endMinute)
                var time = GregorianCalendar(year, month, day, endHour, endMinute)
                txtEndTime = simple.format(cal.time)
                endTime = time.timeInMillis
                var date = Date()
                date.time = time.timeInMillis
                Log.d("TAG", "종료시간은$endTime 심플타임 ${simple.format(date)}")
                // RoomListFragment 리스트의 이름과 방의 이름과 일치하면 좌석 그려주기

                when {

                    endTime < TimeRequest.todayTime() -> {
                        toast(requireContext(), "종료 시각은 현재 시각 이후부터 설정할 수 있습니다.")
                    }

                    startTime > endTime -> {
                        toast(requireContext(), "종료 시각은 대여 시각보다 작을 수 없습니다.")
                    }

                    startTime < endTime -> {
                        for (i in 0 until rooms.rooms.size) {
                            if (name == rooms.rooms[i].roomName) {
                                seatView(rooms.rooms[i].seat, rooms, i)
                            }
                            endOurHour = endHour
                            endOurMinute = endMinute
                            binding.txtEnd.text = txtEndTime
                        }
                    }

                }
            }
            CustomTimePickerDialog(
                requireContext(),
                timeSetListener,
                endOurHour,
                endOurMinute,
                DateFormat.is24HourFormat(requireContext())
            ).show()
        }
    }

    // 정각에 대응하는 timeSet과 년,월,일,요일 Set
    private fun timeSet() {
        var today = Date()
        var dateFormet = SimpleDateFormat("yyyy-MM-dd E")
        binding.txtCurrentTime.text = dateFormet.format(today)

        var calc = Calendar.getInstance()
        if (calc.get(Calendar.MINUTE) + interval == 60) {
            startOurHour = calc.get(Calendar.HOUR_OF_DAY) + 1
            endOurHour = calc.get(Calendar.HOUR_OF_DAY) + 4
            startOurMinute = 0
            endOurMinute = 0
        } else {
            startOurHour = calc.get(Calendar.HOUR_OF_DAY)
            startOurMinute = calc.get(Calendar.MINUTE) + interval
            endOurHour = calc.get(Calendar.HOUR_OF_DAY) + 3
            endOurMinute = calc.get(Calendar.MINUTE) + interval
        }

    }

    private fun setAvailableRecyclerView() {
        var list = arrayListOf(
            Availiable(R.drawable.ic_seats_book, "사용가능"),
            Availiable(R.drawable.ic_seats_reserved, "예약됨"),
            Availiable(R.drawable.ic_seats_booked, "확정됨")
        )

        with(binding.recyclerAvailable) {
            adapter = AvailiableAdapter().apply {
                data = list
                notifyDataSetChanged()
            }
            layoutManager = object : GridLayoutManager(requireContext(), 3) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }

                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            setHasFixedSize(true)
        }
    }

    private fun seatView(room: ArrayList<Array<Int>>, roomData: RoomsData, index: Int) {
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

        layout.addView(layoutSeat)

        var seats = room

        for (i in seats.indices) {
            for (j in seats[i].indices) {
                if (j == START || j == seats.size + 1) {
                    table = LinearLayout(requireContext())
                    table.orientation = LinearLayout.HORIZONTAL
                    layoutSeat.addView(table)
                } else if (seats[i][j] == WALL) {

                } else if (seats[i][j] == EMPTY) {
                    val view = TextView(requireContext())
                    var layoutParams: LinearLayout.LayoutParams =
                        LinearLayout.LayoutParams(seatSize, seatSize)
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

                    table.addView(view)
                    seatViewList.add(view)
                    view.setOnClickListener { click(view) }
                    check = -1
                }

            }
        }

    }

    private fun click(view: View) {
        if (view.tag as Int == STATUS_AVAILABLE) {
            // 다시 눌렀을 시
            if (selectedIds.contains(view.id.toString() + ",")) {
                selectedIds = selectedIds.replace(view.id.toString() + ",", "")
                view.setBackgroundResource(R.drawable.ic_seats_book)
                Log.d("TAG", selectedIds)
            } else { // 선택했을시
                // 하나만 선택가능
                if (selectedIds == "") {
                    selectedIds = selectedIds + view.id + ","
                    seatId = view.id
                    view.setBackgroundResource(R.drawable.ic_seats_selected)
                } else { // 2개이상 선택했을 시
                    toast(requireContext(), "좌석은 하나만 선택 가능합니다.")
                }
                Log.d("TAG", selectedIds)

            }
        } else if (view.tag as Int == STATUS_BOOKED) {
            toast(requireContext(), "Seat " + view.id + " is Booked")
        } else if (view.tag as Int == STATUS_RESERVED) {
            toast(requireContext(), "Seat " + view.id + " is Reserved")
        }
    }

    private fun btnConfirm() {
        binding.btnConfirm.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            var date = Date()
            var endDate = Date()
            date.time = startTime
            endDate.time = endTime
            if (seatId == 0) {
                toast(requireContext(), "좌석을 선택해주세요.")
            } else {
                builder.setTitle("예약메시지")
                    .setMessage("예약시간: ${simple.format(date)} ~ ${simple.format(endDate)}\n좌석번호: $seatId 예약하시겠습니까?")
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                        val dialog = ProgressDialog(requireContext()).apply {
                            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            show()
                        }
                        var input = HashMap<String, Any>()
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

                        RetrofitBuilder.api.reserveRequest(input)
                            .enqueue(object : Callback<Reserve> {
                                override fun onResponse(
                                    call: Call<Reserve>,
                                    response: Response<Reserve>
                                ) {
                                    if (response.isSuccessful) {
                                        Log.d(TAG, response.body()!!.toString())
                                        var reserveResult = response.body()!!
                                        if (reserveResult.result) {
                                            toast(requireContext(), "좌석 예약에 성공하였습니다. 예약시간 10분전에 확정해주세요!")
                                            MySharedPreferences.setConfirmRoomName(
                                                requireContext(),
                                                reserveResult.reservation.roomName
                                            )
                                            MySharedPreferences.setConfirmId(
                                                requireContext(),
                                                reserveResult.reservation.reservationId
                                            )
                                        } else {
                                            toast(requireContext(), response.body()!!.response)
                                        }
                                        findNavController().navigate(ReservationFragmentDirections.actionReservationFragmentToMainFragment())
                                        dialog.dismiss()
                                    }
                                }

                                override fun onFailure(call: Call<Reserve>, t: Throwable) {
                                    Log.d("Error", "연결 에러")
                                    toast(requireContext(), "좌석 예약에 실패하였습니다.")

                                }

                            })
                    })
                    .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                        Log.d("TAG", "취소")
                    })
                builder.create()
                builder.show()
            }


        }

    }

    companion object {
        const val EMPTY = 0
        const val START = 0
        const val DOOR = -2
        const val WALL = -1
        const val STATUS_AVAILABLE = 1
        const val STATUS_BOOKED = 2
        const val STATUS_RESERVED = 3
        var seatGaping = 10
        var count = 0
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        var day = cal.get(Calendar.DAY_OF_MONTH)
        var interval = 10 - (cal.get(Calendar.MINUTE) % 10)
        var simple = SimpleDateFormat("HH시 mm분")
    }

}



