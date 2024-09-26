package com.sgalera.gaztelubira.ui.team.detail

import com.sgalera.gaztelubira.domain.model.PlayerStatsModel
import com.sgalera.gaztelubira.domain.model.players.PlayerStats

sealed class PlayerInformationDetailState {
    data object Loading : PlayerInformationDetailState()
    data class Success(val playerStats: PlayerStatsModel) : PlayerInformationDetailState()
    data class Error(val error: String) : PlayerInformationDetailState()
}
