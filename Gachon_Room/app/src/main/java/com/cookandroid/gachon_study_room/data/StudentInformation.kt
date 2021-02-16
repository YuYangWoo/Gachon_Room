package com.cookandroid.gachon_study_room.data

data class StudentInformation
(
        var name: String,
        var studentId: String,
        var department: String
)
{
    constructor(): this("","","")
}