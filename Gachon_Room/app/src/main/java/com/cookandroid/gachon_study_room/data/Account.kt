package com.cookandroid.gachon_study_room.data

import java.io.Serializable

data class Account(
    var id: String,
    var password: String,
    var name: String,
    var studentId: String,
    var department: String,
    var type: String,
    var college: String
): Serializable {
    constructor(): this("","","","","","","")
}
