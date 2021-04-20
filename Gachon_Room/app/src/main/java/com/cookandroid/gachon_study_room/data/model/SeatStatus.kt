package com.cookandroid.gachon_study_room.data.model

import java.io.Serializable

data class SeatStatus(
    var status: Int,
    var time: Int,
    var pos: Int
): Serializable {
    constructor(): this(0,0, 0)
}
