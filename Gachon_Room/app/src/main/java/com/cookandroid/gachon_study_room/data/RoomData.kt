package com.cookandroid.gachon_study_room.data

import org.json.JSONArray
import org.json.JSONObject

data class RoomData(
        var college: String,
        var name: String,
        var seat: JSONArray,
        var available:JSONArray,
        var reserved: JSONArray
) {
    constructor(): this("", "", JSONArray(), JSONArray(), JSONArray())
}