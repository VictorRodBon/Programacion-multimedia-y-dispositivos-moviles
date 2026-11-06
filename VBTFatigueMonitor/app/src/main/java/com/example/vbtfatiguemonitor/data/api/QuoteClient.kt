package com.example.vbtfatiguemonitor.data.api

import com.example.vbtfatiguemonitor.data.network.QuoteApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object QuoteClient {
    private const val BASE_URL = "https://zenquotes.io/"

    val apiService: QuoteApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteApiService::class.java)
    }
}