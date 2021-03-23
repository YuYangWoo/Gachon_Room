package com.cookandroid.gachon_study_room.data

import java.io.Serializable

data class Information(
    var accout: Account,
    var result: Boolean,
    var message: String
) :Serializable {
    constructor(): this(Account(),false,"")
}
