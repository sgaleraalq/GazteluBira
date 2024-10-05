package com.sgalera.gaztelubira.ui.stats

import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel

sealed class StatsState {
    data object Loading : StatsState()
    data class Success(val data: List<PlayerStatsModel>) : StatsState()
    data class Error(val message: String) : StatsState()
}
