package com.example.coinscope.data.mapper

import com.example.coinscope.data.remote.dto.CoinDto
import com.example.coinscope.domain.model.Coin

fun CoinDto.toDomain(): Coin = Coin(
    id = id,
    symbol = symbol.uppercase(),
    name = name,
    imageUrl = image,
    price = currentPrice
)