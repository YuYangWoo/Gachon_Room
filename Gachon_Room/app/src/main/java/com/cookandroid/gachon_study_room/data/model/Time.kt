package com.cookandroid.gachon_study_room.data.model

import java.io.Serializable

data class Time(
        var time: Long,
        var hour: Int,
        var minute: Int
) :Serializable {
    constructor() : this(0L,0,0)
}
