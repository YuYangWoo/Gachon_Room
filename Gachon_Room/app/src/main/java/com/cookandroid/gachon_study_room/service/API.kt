package com.cookandroid.gachon_study_room.service

import com.cookandroid.gachon_study_room.data.room.RoomsData
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface API {
    @FormUrlEncoded
    @POST("/rooms")
    fun post(@FieldMap param: HashMap<String, String>): Call<RoomsData>
}