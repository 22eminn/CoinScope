package com.example.coinscope.data.remote

import com.example.coinscope.data.remote.dto.CoinDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApi {

    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1
    ): List<CoinDto>

    companion object {
        const val BASE_URL = "https://api.coingecko.com/api/v3/"
    }
}