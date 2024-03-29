package com.cookandroid.gachon_study_room.data.model

import com.cookandroid.gachon_study_room.data.model.room.Room
import java.io.Serializable

data class Reserve(
        var result: Boolean,
        var reservation: Room.Reservation,
        var response: String
): Serializable {
    constructor(): this(false, Room.Reservation(), "")
}
