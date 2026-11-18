package com.example.coinscope.presentation.list

import com.example.coinscope.domain.model.Coin

data class CoinListUiState(
    val query: String = "",
    val coins: List<Coin> = emptyList(),
    val isLoading: Boolean = false,
    val hasError: Boolean = false
)
