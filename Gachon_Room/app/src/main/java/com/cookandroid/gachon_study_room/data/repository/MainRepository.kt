package com.cookandroid.gachon_study_room.data.repository

import com.cookandroid.gachon_study_room.data.api.RetroInstance

class MainRepository {
    suspend fun rooms(data: HashMap<String, Any>) = RetroInstance.client.requestRooms(data)
}