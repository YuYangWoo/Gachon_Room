package com.cookandroid.gachon_study_room.data.model

import android.accounts.Account
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Account(
    var id: String,
    var password: String,
    var department: String,
    var studentId: String,
    var studentName: String,
    var type: String,
    var college: String
) : Serializable {
    constructor() : this("","","","","","","")
}
