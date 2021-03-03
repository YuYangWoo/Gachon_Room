package com.cookandroid.gachon_study_room.data

data class RoomData(
        var result: Boolean,
        var room : Room,
        var message: String
) {
    constructor(): this(false, Room(),"")
}
