package com.cookandroid.gachon_study_room.data

import java.io.Serializable

data class Reserve(
        var studentId: String,
        var college: String,
        var room: String,
        var seat: String,
        var time: Long,
        var begin: Long,
        var end: Long,
        var id: String,
        var password: String
):Serializable {
    constructor(): this("","","","",0L,0L,0L,"","")
}
