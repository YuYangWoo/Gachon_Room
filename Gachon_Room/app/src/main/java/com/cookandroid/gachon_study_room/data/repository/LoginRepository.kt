package com.cookandroid.gachon_study_room.data.repository

import com.cookandroid.gachon_study_room.data.api.RetroInstance

class LoginRepository {
    suspend fun login(data: HashMap<String, Any>) = RetroInstance.client.requestLogin(data)
}