package com.cookandroid.gachon_study_room.data

data class StudentInformation
(
        var name: String,
        var studentId: String,
        var department: String,
        var college: String
)
{
    constructor(): this("","","", "")
}