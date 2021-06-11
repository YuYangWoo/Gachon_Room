package com.cookandroid.gachon_study_room.data.model

import java.io.Serializable

data class LoginRequest(
        var id: String = "",
        var password: String = ""
): Serializable {
    constructor(): this("","")
}