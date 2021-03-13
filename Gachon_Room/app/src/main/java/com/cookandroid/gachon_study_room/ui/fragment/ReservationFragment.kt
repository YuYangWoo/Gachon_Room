package com.cookandroid.gachon_study_room.ui.fragment

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.adapter.AvailiableAdapter
import com.cookandroid.gachon_study_room.data.room.Availiable
import com.cookandroid.gachon_study_room.data.room.RoomsData
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
    var selectedIds = ""

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
        setRecyclerView()
    }

    private fun setRecyclerView() {
        var list = arrayListOf(
                Availiable(R.drawable.ic_seats_book, "사용가능"),
                Availiable(R.drawable.ic_seats_reserved, "예약됨"),
                Availiable(R.drawable.ic_seats_booked, "확정된 좌석")
        )

        with(binding.recyclerAvailable) {
            adapter = AvailiableAdapter().apply {
                data = list
                notifyDataSetChanged()
            }
            layoutManager = LinearLayoutManager(requireContext())
            (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
            setHasFixedSize(true)
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

                    // 빈좌석일 때
                    if (roomData.rooms[index].reserved[seats[i][j]].toString() == "[]") {
                        view.setBackgroundResource(R.drawable.ic_seats_book)
                        view.tag = STATUS_AVAILABLE
                    } else {
                        // 확정되었을 때
                        if (roomData.rooms[index].reserved[seats[i][j]].contains("confirmed")) {
                            view.setBackgroundResource(R.drawable.ic_seats_booked)
                            view.tag = STATUS_BOOKED
                            // 예약만하고 확정 되지 않았을 때
                        } else if (!roomData.rooms[index].reserved[seats[i][j]].contains("confirmed")) {
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
                    view.setOnClickListener { click(view) }
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
            toast("Seat " + view.id + " is Booked")
        } else if (view.tag as Int == STATUS_RESERVED) {
            toast("Seat " + view.id + " is Reserved")
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