package com.example.wiseai.service

import com.example.wiseai.data.CompletionRequest
import com.example.wiseai.data.CompletionResponse
import com.example.wiseai.service.API_KEY.MY_API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenIAApi {

    @Headers("Authorization: Bearer $MY_API_KEY")
    @POST("v1/completions")
    suspend fun getResponse(@Body completionResponse: CompletionRequest): Response<CompletionResponse>
}