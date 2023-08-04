package com.example.wiseai.service

import okhttp3.OkHttp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object APIService {

    const val BASE_URL = "https://api.openai.com/"

    private val httpService = OkHttpClient.Builder()
        .build()

    private val retrofitService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpService)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: OpenIAApi = retrofitService.create(OpenIAApi::class.java)
}