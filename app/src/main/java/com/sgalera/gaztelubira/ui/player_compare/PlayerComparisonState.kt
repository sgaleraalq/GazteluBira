package com.sgalera.gaztelubira.ui.player_compare

import com.sgalera.gaztelubira.domain.model.players.PlayerStats

sealed class PlayerComparisonState {
    data object Loading: PlayerComparisonState()
    data class Success(val playerStats: PlayerStats): PlayerComparisonState()
    data class Error(val error: String): PlayerComparisonState()

}
