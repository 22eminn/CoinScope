package com.example.coinscope.domain.repository

import com.example.coinscope.domain.model.Coin

interface CoinRepository {
    suspend fun getCoins(): Result<List<Coin>>
    suspend fun searchCoins(query: String): Result<List<Coin>>
    suspend fun getCoin(id: String): Result<Coin>
}