package com.example.coinscope.data.remote.dto

data class SearchResponseDto(
    val coins: List<SearchCoinDto>
)

data class SearchCoinDto(
    val id: String
)