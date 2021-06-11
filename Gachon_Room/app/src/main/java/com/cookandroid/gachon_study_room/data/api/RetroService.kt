package com.cookandroid.gachon_study_room.data.api

import com.cookandroid.gachon_study_room.data.model.*
import com.cookandroid.gachon_study_room.data.model.room.RoomsData
import retrofit2.Response
import retrofit2.http.*

interface RetroService {

    @FormUrlEncoded
    @POST("/rooms")
    suspend fun requestRooms(@FieldMap param: HashMap<String, Any>): Response<RoomsData>

    @POST("/account/signIn")
    suspend fun requestLogin(@Body loginRequest: LoginRequest): Response<Account>

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