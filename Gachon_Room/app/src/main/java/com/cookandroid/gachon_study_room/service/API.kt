package com.cookandroid.gachon_study_room.service

import com.cookandroid.gachon_study_room.data.Information
import com.cookandroid.gachon_study_room.data.room.RoomsData
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface API {
    @FormUrlEncoded
    @POST("/rooms")
    fun roomsRequest(@FieldMap param: HashMap<String, String>): Call<RoomsData>

    @FormUrlEncoded
    @POST("/login")
    fun loginRequest(@FieldMap param: HashMap<String, String>): Call<Information>
}