package com.sgalera.gaztelubira.domain.model

import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

data class PlayerStatsModel(
    val information: PlayerInformation?, // TODO
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