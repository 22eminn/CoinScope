package com.example.coinscope.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinscope.domain.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val repository: CoinRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CoinListUiState())
    val state: StateFlow<CoinListUiState> = _state.asStateFlow()

    private val query = MutableStateFlow("")

    init {
        observeQuery()
    }

    fun onQueryChange(newQuery: String) {
        query.value = newQuery
        _state.update { it.copy(query = newQuery) }
    }

    fun onClearQuery() = onQueryChange("")

    fun retry() {
        viewModelScope.launch { load(query.value.trim()) }
    }

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        viewModelScope.launch {
            query
                .map { it.trim() }
                .debounce { if (it.isEmpty()) 0L else 500L }
                .distinctUntilChanged()
                .collectLatest { load(it) }
        }
    }

    private suspend fun load(q: String) {
        _state.update { it.copy(isLoading = true, hasError = false) }

        val result = if (q.isEmpty()) repository.getCoins()
        else repository.searchCoins(q)

        result
            .onSuccess { coins ->
                _state.update { it.copy(coins = coins, isLoading = false) }
            }
            .onFailure {
                _state.update { it.copy(isLoading = false, hasError = true) }
            }
    }
}