package com.example.vbtfatiguemonitor.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class QuoteResponse(val quote: String, val author: String)

interface QuoteApiService {
    @GET("api/quotes/random") // Ejemplo de endpoint de API pública
    suspend fun getRandomQuote(): QuoteResponse

    companion object {
        fun create(): QuoteApiService {
            return Retrofit.Builder()
                .baseUrl("https://dummyjson.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuoteApiService::class.java)
        }
    }
}
