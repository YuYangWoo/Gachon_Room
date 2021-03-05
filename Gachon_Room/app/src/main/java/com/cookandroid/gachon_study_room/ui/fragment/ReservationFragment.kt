package com.cookandroid.gachon_study_room.ui.fragment

import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.android.volley.RequestQueue
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.Room
import com.cookandroid.gachon_study_room.data.RoomsData
import com.cookandroid.gachon_study_room.databinding.FragmentReservationBinding
import com.cookandroid.gachon_study_room.ui.base.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*
import kotlin.collections.ArrayList


class ReservationFragment : BaseFragment<FragmentReservationBinding>(R.layout.fragment_reservation) {
    private val args: ReservationFragmentArgs by navArgs()
    private lateinit var layout: ViewGroup
    private var seatViewList = ArrayList<TextView>()
    var seatSize = 100
    private lateinit var queue: RequestQueue
    private lateinit var rooms: RoomsData
    private lateinit var name: String
    private var collegeRoom = ArrayList<Room>()
    override fun init() {
        super.init()
        layout = binding.layoutSeat
        rooms = args.rooms
        name = args.name
//        val prev: Fragment = (context as AppCompatActivity).supportFragmentManager.findFragmentByTag("Modal")!!
//        if (prev != null) {
//            val df: BottomSheetDialogFragment = prev as BottomSheetDialogFragment
//            df.dismiss()
//        }
        Log.d("test", name)
        Log.d("test", rooms.rooms[0].seat.toString())
//        queue = Volley.newRequestQueue(context)
//        seperateCollege(rooms)
        for(i in 0 until rooms.rooms.size) {
            if(name == rooms.rooms[i].name) {
                seatView(rooms.rooms[i].seat)

            }
        }
    }

//    private fun seperateCollege(rooms: RoomsData) {
//        var name = ArrayList<String>()
//        for (i in 0 until rooms.rooms.size) {
//            if (rooms.rooms[i].college == "TEST") {
//                collegeRoom.add(rooms.rooms[i])
//                name.add(rooms.rooms[i].name)
//            }
//        }
//        var arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, name)
//        binding.roomList.adapter = arrayAdapter
//        binding.roomList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                for (i in 0 until name.size) {
//                    if (name[position] == collegeRoom[i].name) {
////                       for(intArray in collegeRoom[i].seat) {
////                           for(int in intArray) {
////                               print("$int")
////                           }
////                           println()
////                       }
//                        seatView(collegeRoom[i].seat)
//                    }
//                }
//            }
//
//        }
//    }

    private fun seatView(room: ArrayList<Array<Int>>) {
        val layoutSeat = LinearLayout(requireContext())
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutSeat.orientation = LinearLayout.VERTICAL
        layoutSeat.layoutParams = params
        layoutSeat.setPadding(seatGaping, seatGaping, seatGaping, seatGaping)
        layout.addView(layoutSeat)
        lateinit var layout: LinearLayout
        var seats = room
        var garo = seats[0].size //행
        Log.d("test", "garo" + garo.toString())
        var sero = seats.size // 열
        Log.d("test", "sero" + sero.toString())
//        for(intArray in seats) {
//            for(int in intArray) {
//                print("$int")
//            }
//            println()
//        }

        for (i in seats.indices) {
            for (j in seats[i].indices) {
                if (j == 0 || j == sero) {
                    layout = LinearLayout(requireContext())
                    layout.orientation = LinearLayout.HORIZONTAL
                    layoutSeat.addView(layout)
                    if (seats[i][j] == WALL) {
                        val view = TextView(requireContext())
                        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                        layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                        view.layoutParams = layoutParams
                        view.setBackgroundColor(Color.TRANSPARENT)
                        layout.addView(view)
                    }
                } else if (seats[i][j] == WALL) {
                    val view = TextView(requireContext())
                    val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                    view.layoutParams = layoutParams
                    view.setBackgroundColor(Color.TRANSPARENT)
                    layout.addView(view)
                } else if (seats[i][j] == EMPTY) {
                    val view = TextView(requireContext())
                    val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                    view.layoutParams = layoutParams
                    view.setBackgroundColor(Color.TRANSPARENT)
                    layout.addView(view)
                } else if (seats[i][j] == DOOR) {
                    count++
                    val view = TextView(requireContext())
                    val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                    view.layoutParams = layoutParams
                    view.id = count
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
                    view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
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