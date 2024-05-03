package com.sgalera.gaztelubira.ui.team

import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

sealed class TeamInfoState {
    data object Loading : TeamInfoState()
    data class Success(val playerList: List<PlayerInformation>) : TeamInfoState()
    data class Error(val error: String) : TeamInfoState()
}