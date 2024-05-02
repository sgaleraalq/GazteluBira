package com.sgalera.gaztelubira.ui.insert_game

import com.sgalera.gaztelubira.domain.model.TeamInformation

sealed class InsertGameState {
    data object Loading : InsertGameState()
    data class Success(val teams: List<TeamInformation>) : InsertGameState()
    data class Error(val message: String) : InsertGameState()
}