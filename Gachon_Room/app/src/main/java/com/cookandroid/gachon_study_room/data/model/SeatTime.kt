package com.cookandroid.gachon_study_room.data.model

import java.io.Serializable

data class SeatTime(
    var startTime: ArrayList<ArrayList<Long>>,
    var endTime: ArrayList<ArrayList<Long>>,

//    var unStart: ArrayList<Int>,
//    var unEnd: ArrayList<Int>
) : Serializable {
    constructor() : this(arrayListOf(), arrayListOf())
}