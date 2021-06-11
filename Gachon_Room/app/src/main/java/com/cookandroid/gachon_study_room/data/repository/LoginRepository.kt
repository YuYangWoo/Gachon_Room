package com.cookandroid.gachon_study_room.data.repository

import com.cookandroid.gachon_study_room.data.api.RetroInstance
import com.cookandroid.gachon_study_room.data.model.LoginRequest

class LoginRepository {
    suspend fun login(data: LoginRequest) = RetroInstance.client.requestLogin(data)
}