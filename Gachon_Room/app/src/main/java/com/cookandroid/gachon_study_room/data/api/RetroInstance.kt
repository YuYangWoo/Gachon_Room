package com.cookandroid.gachon_study_room.data.api

object RetroInstance {
    val baseUrl = "http://192.168.41.107:8080/"
    val client = BaseRetro.getClient(baseUrl).create(RetroService::class.java)
}