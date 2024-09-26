package com.sgalera.gaztelubira.domain.model

data class PlayerStatsModel(
    val information: PlayerModel?, // TODO
    val position: String,
    val goals: Int,
    val assists: Int,
    val penalties: Int,
    val cleanSheet: Int,
    val gamesPlayed: Int,
    val ranking: Int,
    val lastRanking: Int,
    val percentage: String
)