package com.sgalera.gaztelubira.ui.matches.detail

import com.sgalera.gaztelubira.domain.model.matches.Match

sealed class DetailMatchState{
    data object Loading: DetailMatchState()
    data class Error(val error: String): DetailMatchState()

    data class Success(val match: Match): DetailMatchState()
}