package com.sgalera.gaztelubira.ui.insert_game

sealed class InsertGameInfoState {
    object Loading : InsertGameInfoState()
    object Success : InsertGameInfoState()
    data class Error(val message: String) : InsertGameInfoState()
}
