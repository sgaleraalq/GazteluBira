package com.sgalera.gaztelubira.domain.model.players

data class PlayerStats(
    val name: PlayerInfo,
    val goals: Int,
    val assists: Int,
    val gamesPlayed: Int,
    val penalties: Int,
    val cleanSheet: Int,
    val position: String,
    val lastRanking: Int,
    val ranking: Int,
    var percentage: String? = null
)