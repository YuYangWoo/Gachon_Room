package com.cookandroid.gachon_study_room.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BaseRetro {
    var gson = GsonBuilder().setLenient().create()
    fun getClient(baseUrl: String): Retrofit {
        return Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .client(OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }
}