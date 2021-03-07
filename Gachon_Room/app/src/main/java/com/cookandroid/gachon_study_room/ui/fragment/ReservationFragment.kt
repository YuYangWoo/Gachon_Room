package com.cookandroid.gachon_study_room.ui.fragment

import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.navArgs
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.RoomsData
import com.cookandroid.gachon_study_room.databinding.FragmentReservationBinding
import com.cookandroid.gachon_study_room.ui.base.BaseFragment


class ReservationFragment : BaseFragment<FragmentReservationBinding>(R.layout.fragment_reservation) {
    private val args: ReservationFragmentArgs by navArgs()
    private lateinit var layout: ViewGroup
    private var seatViewList = ArrayList<TextView>()
    private var seatSize = 100
    private lateinit var rooms: RoomsData
    private lateinit var name: String
    private lateinit var table: LinearLayout
    override fun init() {
        super.init()
        layout = binding.layoutSeat
        rooms = args.rooms
        name = args.name

        for (i in 0 until rooms.rooms.size) {
            if (name == rooms.rooms[i].name) {
                seatView(rooms.rooms[i].seat, rooms, i)
            }
        }

    }

    private fun seatView(room: ArrayList<Array<Int>>, roomData: RoomsData, index: Int) {
        val layoutSeat = LinearLayout(requireContext())
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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
                    val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                    view.layoutParams = layoutParams
                    view.setBackgroundColor(Color.TRANSPARENT)
                    table.addView(view)
                } else if (seats[i][j] == DOOR) {
                    count++
                    val view = TextView(requireContext())
                    val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                    view.layoutParams = layoutParams
                    view.id = count
                    view.setBackgroundResource(R.drawable.door)
                    table.addView(view)
                    seatViewList.add(view)
                } else {
                    val view = TextView(requireContext())
                    val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)

                    // 빈좌석일 때
                    if(roomData.rooms[index].reserved[seats[i][j]].toString() == "[]") {
                        view.setBackgroundResource(R.drawable.ic_seats_book)
                        view.tag = STATUS_AVAILABLE
                    }
                    else{
                        // 확정되었을 때
                            Log.d("test", "빈좌석이아니다.")
                    if (roomData.rooms[index].reserved[seats[i][j]].contains("confirmed")) {
                        view.setBackgroundResource(R.drawable.ic_seats_booked)
                        view.tag = STATUS_BOOKED
                        // 예약만하고 확정 되지 않았을 때
                    } else if (!roomData.rooms[index].reserved[seats[i][j]].contains("confirmed")) {
                        Log.d("test", "예약좌석이다.")
                        view.setBackgroundResource(R.drawable.ic_seats_reserved)
                        view.tag = STATUS_RESERVED
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
                }

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
    }


}