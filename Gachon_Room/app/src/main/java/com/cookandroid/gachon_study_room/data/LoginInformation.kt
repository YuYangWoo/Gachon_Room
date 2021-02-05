package com.cookandroid.gachon_study_room.data

data class LoginInformation(
        var id: String,
        var password: String,
        var type: String
){
    constructor():this("","","")
}