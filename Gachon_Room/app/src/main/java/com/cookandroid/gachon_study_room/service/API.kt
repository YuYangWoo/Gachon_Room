package com.cookandroid.gachon_study_room.service

import com.cookandroid.gachon_study_room.data.model.Confirm
import com.cookandroid.gachon_study_room.data.model.Information
import com.cookandroid.gachon_study_room.data.model.MySeat
import com.cookandroid.gachon_study_room.data.model.Reserve
import com.cookandroid.gachon_study_room.data.model.room.RoomsData
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import kotlin.collections.HashMap

interface API {
    @FormUrlEncoded
    @POST("/rooms")
    fun roomsRequest(@FieldMap param: HashMap<String, String>): Call<RoomsData>

    @FormUrlEncoded
    @POST("/account/login")
    fun loginRequest(@FieldMap param: HashMap<String, String>): Call<Information>

    @FormUrlEncoded
    @POST("/room/reserve/my")
    fun mySeatRequest(@FieldMap param: HashMap<String, String>): Call<MySeat>

    @FormUrlEncoded
    @POST("/room/reserve")
    fun reserveRequest(@FieldMap param: HashMap<String, Any>): Call<Reserve>

    @FormUrlEncoded
    @POST("/room/reserve/cancel")
    fun back(@FieldMap param: HashMap<String, Any>): Call<MySeat>

    @FormUrlEncoded
    @POST("/room/reserve/confirm")
    fun confirm(@FieldMap param: HashMap<String, Any>): Call<Confirm>
}