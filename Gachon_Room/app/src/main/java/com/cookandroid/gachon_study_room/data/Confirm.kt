package com.cookandroid.gachon_study_room.data

import com.cookandroid.gachon_study_room.data.room.Room
import java.io.Serializable

data class Confirm(
        var result: Boolean,
        var response: String,
        var reservation: Room.Reservation
): Serializable
{
    constructor(): this(false, "", Room.Reservation())
}
