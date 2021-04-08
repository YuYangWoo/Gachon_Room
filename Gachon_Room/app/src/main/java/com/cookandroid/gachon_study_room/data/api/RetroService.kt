package com.cookandroid.gachon_study_room.data.api

import com.cookandroid.gachon_study_room.data.model.Information
import com.cookandroid.gachon_study_room.data.model.room.RoomsData
import com.cookandroid.gachon_study_room.util.Resource
import retrofit2.Response
import retrofit2.http.*

interface RetroService {

    @FormUrlEncoded
    @POST("/rooms")
    suspend fun getRooms(@FieldMap college: HashMap<String, Any>): Response<RoomsData>

    @FormUrlEncoded
    @POST("/account/login")
    suspend fun requestLogin(@FieldMap info: HashMap<String, Any>): Response<Information>
}