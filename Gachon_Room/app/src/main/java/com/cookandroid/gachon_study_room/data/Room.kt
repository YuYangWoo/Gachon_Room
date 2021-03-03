package com.cookandroid.gachon_study_room.data

data class Room(
        var college: String,
        var name: String,
        var seat: ArrayList<Array<Int>>, //2차원
        var available: ArrayList<Boolean>,
        var reserved: ArrayList<MutableSet<Reservation>>,
        var confirmTimeLimit: Long
) {
    constructor(): this("","", arrayListOf(), arrayListOf(), arrayListOf(), 0)
}