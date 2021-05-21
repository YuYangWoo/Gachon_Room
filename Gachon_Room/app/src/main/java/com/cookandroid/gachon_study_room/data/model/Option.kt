package com.cookandroid.gachon_study_room.data.model

import java.io.Serializable

data class Option(
    var img: String,
    var topic: String,
): Serializable {
    constructor(): this("","")
}