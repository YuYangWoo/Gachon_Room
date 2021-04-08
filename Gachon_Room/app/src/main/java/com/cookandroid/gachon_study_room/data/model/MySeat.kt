package com.cookandroid.gachon_study_room.data.model

import com.cookandroid.gachon_study_room.data.model.room.Room
import java.io.Serializable

data class MySeat(
        var result: Boolean,
        var reservations: ArrayList<Room.Reservation>,
        var response: String
): Serializable {
    constructor(): this(false, arrayListOf(), "")
}
