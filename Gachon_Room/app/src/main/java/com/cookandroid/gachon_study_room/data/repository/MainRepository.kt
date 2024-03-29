package com.cookandroid.gachon_study_room.data.repository

import com.cookandroid.gachon_study_room.data.api.RetroInstance

class MainRepository {
    suspend fun rooms(data: HashMap<String, Any>) = RetroInstance.client.requestRooms(data)
    suspend fun reserve(data: HashMap<String, Any>) = RetroInstance.client.requestReserve(data)
    suspend fun mySeat(data: HashMap<String, Any>) = RetroInstance.client.requestMySeat(data)
    suspend fun cancel(data: HashMap<String, Any>) = RetroInstance.client.requestCancel(data)
    suspend fun confirm(data: HashMap<String, Any>) = RetroInstance.client.requestConfirm(data)
    suspend fun extend(data: HashMap<String, Any>) = RetroInstance.client.requestExtend(data)
}