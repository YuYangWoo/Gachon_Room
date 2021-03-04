package com.cookandroid.gachon_study_room.ui.fragment

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.RoomData
import com.cookandroid.gachon_study_room.databinding.FragmentReservationBinding
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import java.util.*

class ReservationFragment : BaseFragment<FragmentReservationBinding>(R.layout.fragment_reservation) {
    private val args: ReservationFragmentArgs by navArgs()
    private lateinit var layout: ViewGroup
    private var seatViewList = ArrayList<TextView>()
    var seatSize = 200

    override fun init() {
        super.init()

        layout = binding.layoutSeat
        var room = args.room
        seatView(room)
    }

    private fun seatView(room: RoomData) {
        val layoutSeat = LinearLayout(requireContext())
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutSeat.orientation = LinearLayout.VERTICAL
        layoutSeat.layoutParams = params
        layoutSeat.setPadding(seatGaping, seatGaping, seatGaping, seatGaping)
        layout.addView(layoutSeat)
        lateinit var layout: LinearLayout

        var garo = room.room.seat[0].size //행
        var sero = room.room.seat.size // 열

        var seats = room.room.seat
        for (i in 0 until garo) {
            for (j in 0 until sero) {
                if (j == 0 || j == sero) {
                    layout = LinearLayout(requireContext())
                    layout.orientation = LinearLayout.HORIZONTAL
                    layoutSeat.addView(layout)
                    if(seats[i][j] == WALL) {
                        val view = TextView(requireContext())
                        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                        layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                        view.layoutParams = layoutParams
                        view.setBackgroundColor(Color.BLACK)
                        view.text = ""
                        layout.addView(view)
                    }
                } else if (seats[i][j] == WALL) {
                    val view = TextView(requireContext())
                    val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                    view.layoutParams = layoutParams
                    view.setBackgroundColor(Color.BLACK)
                    view.text = ""
                    layout.addView(view)
                } else if (seats[i][j] == EMPTY) {
                    val view = TextView(requireContext())
                    val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                    view.layoutParams = layoutParams
                    view.setBackgroundColor(Color.TRANSPARENT)
                    view.text = ""
                    layout.addView(view)
                } else if (seats[i][j] == DOOR) {
                    count++
                    val view = TextView(requireContext())
                    val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                    view.layoutParams = layoutParams
                    view.setPadding(0, 0, 0, 2 * seatGaping)
                    view.id = count
                    view.gravity = Gravity.CENTER
                    view.setBackgroundResource(R.drawable.door)
                    layout.addView(view)
                    seatViewList.add(view)
                } else {
                    val view = TextView(requireContext())
                    val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                    view.layoutParams = layoutParams
                    view.setPadding(0, 0, 0, 2 * seatGaping)
                    view.id = seats[i][j]
                    view.gravity = Gravity.CENTER
                    view.setBackgroundResource(R.drawable.ic_seats_book)
                    view.setTextColor(Color.BLACK)
                    view.text = seats[i][j].toString()
                    view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
                    layout.addView(view)
                    seatViewList.add(view)
                }

            }
        }

    }

    companion object {
        const val EMPTY = 0
        const val DOOR = -2
        const val WALL = -1
        var seatGaping = 10
        var count = 0
    }
}