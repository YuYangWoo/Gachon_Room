package com.cookandroid.gachon_study_room.data.room

import android.widget.ImageView
import java.io.Serializable

data class Availiable(
        var img: Int,
        var txt: String
) : Serializable {
    constructor() : this(0, "")
}
