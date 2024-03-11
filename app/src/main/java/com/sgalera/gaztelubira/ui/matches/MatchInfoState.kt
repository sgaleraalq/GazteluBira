package com.sgalera.gaztelubira.ui.matches

import com.sgalera.gaztelubira.domain.model.matches.MatchInfo

sealed class MatchInfoState {
    data object Loading : MatchInfoState()
    data class Error(val error: String) : MatchInfoState()
    data class Success(val matchesList: List<MatchInfo>) : MatchInfoState()
}
