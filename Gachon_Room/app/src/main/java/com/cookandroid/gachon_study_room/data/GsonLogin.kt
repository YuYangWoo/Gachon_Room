package com.cookandroid.gachon_study_room.data

data class GsonLogin(
        var result: Boolean,
        var message: String,
        var account: LoginInformation
) {
    constructor() : this(false, "", LoginInformation())
}
