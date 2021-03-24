package com.cookandroid.gachon_study_room.data.room

import java.io.Serializable

data class Room(
        var college: String,
        var name: String,
        var seat: ArrayList<Array<Int>>, //2차원
        var available: ArrayList<Boolean>,
        var reserved: List<ArrayList<Reservation>>,
        var confirmTimeLimit: Long
) : Serializable {
    constructor() : this("", "", arrayListOf(), arrayListOf(), arrayListOf(), 0)

    data class Reservation(
            var studentId: String,
            var college: String,
            var room: String,
            var seat: Int,
            var time: Long,
            var begin: Long,
            var end: Long,
            var confirmed: Boolean
    ) : Serializable {
        constructor() : this("", "", "", 0, 0, 0, 0, false)
    }

}