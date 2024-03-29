package com.cookandroid.gachon_study_room.data.model.room

import java.io.Serializable

data class RoomsData(
        var result: Boolean,
        var rooms: ArrayList<Room>,
        var response: String
) : Serializable {
    constructor() : this(false, arrayListOf(), "")
}
