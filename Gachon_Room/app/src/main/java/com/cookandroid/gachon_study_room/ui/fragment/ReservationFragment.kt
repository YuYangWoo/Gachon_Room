package com.cookandroid.gachon_study_room.ui.fragment

import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.adapter.AvailiableAdapter
import com.cookandroid.gachon_study_room.data.room.Availiable
import com.cookandroid.gachon_study_room.data.room.RoomsData
import com.cookandroid.gachon_study_room.databinding.FragmentReservationBinding
import com.cookandroid.gachon_study_room.singleton.TimeRequest
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import com.cookandroid.gachon_study_room.ui.dialog.CustomTimePickerDialog
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ReservationFragment : BaseFragment<FragmentReservationBinding>(R.layout.fragment_reservation) {
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun init() {
        super.init()
        layout = binding.layoutSeat
        rooms = args.rooms
        name = args.name

        startTime = TimeRequest.timeLong().time
        endTime = TimeRequest.endTimeLong().time

        // RoomListFragment 리스트의 이름과 방의 이름과 일치하면 좌석 그려주기
        for (i in 0 until rooms.rooms.size) {
            if (name == rooms.rooms[i].name) {
                seatView(rooms.rooms[i].seat, rooms, i)
            }
        }
        setAvailableRecyclerView()
        timeSet()
        btnStart()
        btnEnd()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun btnStart() {
        binding.txtStart.text = TimeRequest.time()
        binding.cardViewStart.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            var day = cal.get(Calendar.DAY_OF_MONTH)
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            var interval = 10 - (cal.get(Calendar.MINUTE) % 10)
            val minute = cal.get(Calendar.MINUTE) + interval
            // 아래 hour, minute가 선택된 시간 분
            var timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker,
                                                                       hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                var myStringInfo = SimpleDateFormat("HH시 mm분").format(cal.time)
                var time = GregorianCalendar(year, month, day, hour, minute)
                startTime = time.timeInMillis
                Log.d("TAG", "시작시간은" + startTime.toString())
                // RoomListFragment 리스트의 이름과 방의 이름과 일치하면 좌석 그려주기
                if(startTime < endTime) {
                    for (i in 0 until rooms.rooms.size) {
                        if (name == rooms.rooms[i].name) {
                            seatView(rooms.rooms[i].seat, rooms, i)
                        }
                    }
                    // ok 버트 누르고 나오는 시간.
                    startOurHour = hour
                    startOurMinute = minute
                    binding.txtStart.text = myStringInfo
                }
                else {
                    toast(requireContext(), "시작시간이 종료시간 보다 늦을 수 없습니다.")
                }

            }
            // 이부분 초기 설정 값으로 넣어주기. 아래 hour minute가 다이얼로그 나타날때 뜨는 시간
            if(startOurHour == 0) {
                CustomTimePickerDialog(requireContext(), timeSetListener, hour, minute, DateFormat.is24HourFormat(requireContext())).show()
            }
            else {
                CustomTimePickerDialog(requireContext(), timeSetListener, startOurHour, startOurMinute, DateFormat.is24HourFormat(requireContext())).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun btnEnd() {
        binding.txtEnd.text = TimeRequest.endTime()
        binding.cardViewEnd.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            var day = cal.get(Calendar.DAY_OF_MONTH)
            var endhour = cal.get(Calendar.HOUR_OF_DAY) + 3
            var interval = 10 - (cal.get(Calendar.MINUTE) % 10)
            val endminute = cal.get(Calendar.MINUTE) + interval
            var timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker,
                                                                       endHour, endMinute ->

                cal.set(Calendar.HOUR_OF_DAY, endHour)
                cal.set(Calendar.MINUTE, endMinute)
                var time = GregorianCalendar(year, month, day, endHour, endMinute)
                var myStringInfo = SimpleDateFormat("HH시 mm분").format(cal.time)
                endTime = time.timeInMillis
                Log.d("TAG", "종료 시간은" + endTime.toString())
                // RoomListFragment 리스트의 이름과 방의 이름과 일치하면 좌석 그려주기
                if(startTime < endTime) {
                    for (i in 0 until rooms.rooms.size) {
                        if (name == rooms.rooms[i].name) {
                            seatView(rooms.rooms[i].seat, rooms, i)
                        }
                        endOurHour = endHour
                        endOurMinute = endMinute
                        binding.txtEnd.text = myStringInfo
                    }
                }
                else {
                    toast(requireContext(), "시작시간이 종료시간 보다 늦을 수 없습니다.")
                }

            }
            if(endOurHour == 0) {
                CustomTimePickerDialog(requireContext(), timeSetListener, endhour, endminute, DateFormat.is24HourFormat(requireContext())).show()
            }
            else {
                CustomTimePickerDialog(requireContext(), timeSetListener, endOurHour, endOurMinute, DateFormat.is24HourFormat(requireContext())).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun timeSet() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd E")
        binding.txtCurrentTime.text = current.format(formatter)
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
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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
                    var layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                    view.layoutParams = layoutParams
                    view.setBackgroundColor(Color.TRANSPARENT)

                    table.addView(view)
                } else if (seats[i][j] == DOOR) {
                    val view = TextView(requireContext())
                    var layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)

                    view.layoutParams = layoutParams
                    view.id = count
                    view.setBackgroundResource(R.drawable.door)

                    table.addView(view)
                    seatViewList.add(view)
                } else {
                    val view = TextView(requireContext())
                    var layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)

                    // 좌석의 번호에 따른 reserved 크기를 구해 예약된 시간과 비교
                    for(z in 0 until roomData.rooms[index].reserved[seats[i][j]].size) {
                        // 예약하려는 시작시간이 예약된 종료시간보다 작고 예약하려는 종료시간이 예약된 시간 시작시간보다 크면 reserved confirmed면 booked
                        if(startTime < roomData.rooms[index].reserved[seats[i][j]][z].end && endTime > roomData.rooms[index].reserved[seats[i][j]][z].begin) {
                            check = STATUS_RESERVED
                            if(roomData.rooms[index].reserved[seats[i][j]][z].confirmed) {
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
            if (selectedIds.contains(view.id.toString() + ",")) {
                selectedIds = selectedIds.replace(view.id.toString() + ",", "")
                view.setBackgroundResource(R.drawable.ic_seats_book)
            } else {
                selectedIds = selectedIds + view.id + ","
                view.setBackgroundResource(R.drawable.ic_seats_selected)
            }
        } else if (view.tag as Int == STATUS_BOOKED) {
            toast(requireContext(), "Seat " + view.id + " is Booked")
        } else if (view.tag as Int == STATUS_RESERVED) {
            toast(requireContext(), "Seat " + view.id + " is Reserved")
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
    }

}