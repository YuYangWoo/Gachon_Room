package com.cookandroid.gachon_study_room.data.model

import java.io.Serializable

data class SeatTime(
    var seatId: Int,
    var startTime: Long,
    var endTime: Long
) : Serializable {
    constructor() : this(0, 0L, 0L)
}