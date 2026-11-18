package com.example.coinscope.data.repository

import com.example.coinscope.data.mapper.toDomain
import com.example.coinscope.data.remote.CoinGeckoApi
import com.example.coinscope.domain.model.Coin
import com.example.coinscope.domain.repository.CoinRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinGeckoApi
) : CoinRepository {

    override suspend fun getCoins(): Result<List<Coin>> = safeCall {
        api.getCoins().map { it.toDomain() }
    }

    override suspend fun searchCoins(query: String): Result<List<Coin>> = safeCall {
        val ids = api.search(query).coins
            .take(20)
            .joinToString(",") { it.id }

        if (ids.isEmpty()) emptyList()
        else api.getCoins(ids = ids).map { it.toDomain() }
    }

    override suspend fun getCoin(id: String): Result<Coin> = safeCall {
        api.getCoins(ids = id).firstOrNull()?.toDomain()
            ?: throw NoSuchElementException("Coin not found: $id")
    }

    private suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}