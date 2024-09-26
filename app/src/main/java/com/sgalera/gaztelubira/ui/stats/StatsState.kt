package com.sgalera.gaztelubira.ui.stats

import com.sgalera.gaztelubira.domain.model.players.PlayerStats

sealed class StatsState {
    data object Loading : StatsState()
    data class Success(val data: List<PlayerStats>) : StatsState()
    data class Error(val message: String) : StatsState()
}
