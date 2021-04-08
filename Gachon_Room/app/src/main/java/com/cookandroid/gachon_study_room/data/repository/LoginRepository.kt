package com.cookandroid.gachon_study_room.data.repository

import com.cookandroid.gachon_study_room.data.api.RetroInstance

class LoginRepository(private val retroInstance: RetroInstance) {
    suspend fun login(data: HashMap<String, Any>) = retroInstance.client.requestLogin(data)
}