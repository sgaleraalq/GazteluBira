package com.sgalera.gaztelubira.domain.model.matches

import com.sgalera.gaztelubira.domain.model.TeamInformation

data class MatchInfo (
    val id: Int,
    val match: String,
    val homeTeam: TeamInformation? = null,
    val awayTeam: TeamInformation? = null,
    val homeGoals: Int,
    val awayGoals: Int,
    val journey: String
)