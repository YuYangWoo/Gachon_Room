package com.cookandroid.gachon_study_room.data.repository

import com.cookandroid.gachon_study_room.data.api.RetroInstance

class LoginRepository {
    private val loginClient = RetroInstance.client

    suspend fun login(data: HashMap<String, Any>) = loginClient.requestLogin(data)
}