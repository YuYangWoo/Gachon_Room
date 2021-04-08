package com.cookandroid.gachon_study_room.data.api

object RetroInstance {
    val baseUrl = "http://3.34.174.56:8080"
    val client = BaseRetro.getClient(baseUrl).create(RetroService::class.java)
}