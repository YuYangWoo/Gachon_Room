package com.cookandroid.gachon_study_room.data

data class LoginInformation(
        var id: String,
        var password: String,
        var type: String,
        var name: String,
        var studentId: String,
        var department: String
){
    constructor():this("","","","","","")
}