package com.cookandroid.gachon_study_room.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Account(
    @SerializedName("type")
    var type: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("studentId")
    var studentId: String,
    @SerializedName("college")
    var college: String,
    @SerializedName("department")
    var department: String,
): Serializable {
    constructor(): this("","","","","","","")
}
