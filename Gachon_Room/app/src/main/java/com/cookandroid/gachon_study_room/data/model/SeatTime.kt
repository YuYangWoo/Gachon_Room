package com.cookandroid.gachon_study_room.data.model

import java.io.Serializable

data class SeatTime(
    var startTime: ArrayList<ArrayList<Long>>,
    var endTime: ArrayList<ArrayList<Long>>,
    var unStart: ArrayList<ArrayList<Int>>,
    var unEnd: ArrayList<ArrayList<Int>>
) : Serializable {
    constructor() : this(arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf())
}