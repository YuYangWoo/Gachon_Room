package com.cookandroid.gachon_study_room.data

import java.io.Serializable

data class RoomData(
        var result: Boolean,
        var room : Room,
        var message: String
) :Serializable {
    constructor(): this(false, Room(),"")
}
