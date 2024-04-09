package com.sgalera.gaztelubira.domain.model.matches

data class MatchInfo (
    val id: Int,
    val match: String,
    val homeTeam: String,
    val awayTeam: String,
    val homeGoals: Int,
    val awayGoals: Int,
    val journey: String
)