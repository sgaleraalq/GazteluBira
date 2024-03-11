package com.sgalera.gaztelubira.ui.player_compare

import com.sgalera.gaztelubira.domain.model.players.Player
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo

sealed class PlayerComparisonState {
    data object Loading: PlayerComparisonState()
    data class Success(val player: Player): PlayerComparisonState()
    data class Error(val error: String): PlayerComparisonState()

}
