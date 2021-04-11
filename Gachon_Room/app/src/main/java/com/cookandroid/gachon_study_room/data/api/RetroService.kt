package com.cookandroid.gachon_study_room.data.api

import com.cookandroid.gachon_study_room.data.model.Confirm
import com.cookandroid.gachon_study_room.data.model.Information
import com.cookandroid.gachon_study_room.data.model.MySeat
import com.cookandroid.gachon_study_room.data.model.Reserve
import com.cookandroid.gachon_study_room.data.model.room.RoomsData
import com.cookandroid.gachon_study_room.util.Resource
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetroService {

    @FormUrlEncoded
    @POST("/rooms")
    suspend fun requestRooms(@FieldMap param: HashMap<String, Any>): Response<RoomsData>

    @FormUrlEncoded
    @POST("/account/login")
    suspend fun requestLogin(@FieldMap info: HashMap<String, Any>): Response<Information>

    @FormUrlEncoded
    @POST("/room/reserve")
    suspend fun requestReserve(@FieldMap param: HashMap<String, Any>): Response<Reserve>

    @FormUrlEncoded
    @POST("/room/reserve/cancel")
    suspend fun requestCancel(@FieldMap param: HashMap<String, Any>): Response<MySeat>

    @FormUrlEncoded
    @POST("/room/reserve/confirm")
    suspend fun requestConfirm(@FieldMap param: HashMap<String, Any>): Response<Confirm>

    @FormUrlEncoded
    @POST("/room/reserve/my")
    suspend fun requestMySeat(@FieldMap param: HashMap<String, Any>): Response<MySeat>

    @FormUrlEncoded
    @POST("/room/reserve/extend")
    suspend fun requestExtend(@FieldMap param: HashMap<String, Any>): Response<Reserve>

}