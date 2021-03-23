package com.cookandroid.gachon_study_room.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Information(
    @SerializedName("account")
    var account: Account,
    @SerializedName("result")
    var result: Boolean,
    @SerializedName("message")
    var message: String
) : Serializable {
    constructor() : this(Account(), false, "")
}
