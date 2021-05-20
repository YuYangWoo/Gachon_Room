package com.cookandroid.gachon_study_room.data.api

object RetroInstance {
    val baseUrl = "http://192.168.1.58:8080"
    val client = BaseRetro.getClient(baseUrl).create(RetroService::class.java)
}