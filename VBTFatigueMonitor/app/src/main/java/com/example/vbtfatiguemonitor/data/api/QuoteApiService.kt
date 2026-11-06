package com.example.vbtfatiguemonitor.data.network

import com.example.vbtfatiguemonitor.data.api.QuoteResponse
import retrofit2.http.GET

interface QuoteApiService {
    @GET("api/random")
    suspend fun getRandomQuote(): List<QuoteResponse>
}