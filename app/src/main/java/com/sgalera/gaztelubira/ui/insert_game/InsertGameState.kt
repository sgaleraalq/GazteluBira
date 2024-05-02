package com.sgalera.gaztelubira.ui.insert_game

import com.sgalera.gaztelubira.domain.model.TeamInformation
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

sealed class InsertGameState {
    data object Loading : InsertGameState()
    data class SuccessTeams(val teams: List<TeamInformation>) : InsertGameState()
    data class SuccessPlayers(val players: MutableList<PlayerInformation>) : InsertGameState()
    data class Error(val message: String) : InsertGameState()
}