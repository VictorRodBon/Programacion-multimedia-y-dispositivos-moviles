package com.example.vbtfatiguemonitor.data.api

import com.google.gson.annotations.SerializedName

data class QuoteResponse(
    @SerializedName("q")
    val quote: String, // El texto de la frase
    @SerializedName("a")
    val author: String // El autor
)