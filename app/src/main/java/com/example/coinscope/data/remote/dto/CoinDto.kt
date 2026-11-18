package com.example.coinscope.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CoinDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String?,
    @SerializedName("current_price") val currentPrice: Double?
)