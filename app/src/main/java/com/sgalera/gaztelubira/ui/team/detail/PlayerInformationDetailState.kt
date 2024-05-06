package com.sgalera.gaztelubira.ui.team.detail

import com.sgalera.gaztelubira.domain.model.players.PlayerStats

sealed class PlayerInformationDetailState {
    object Loading : PlayerInformationDetailState()
    data class Success(val playerStats: PlayerStats) : PlayerInformationDetailState()
    data class Error(val error: String) : PlayerInformationDetailState()
}
