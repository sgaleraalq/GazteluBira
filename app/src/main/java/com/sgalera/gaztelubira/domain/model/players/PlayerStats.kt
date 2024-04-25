package com.sgalera.gaztelubira.domain.model.players

data class PlayerStats(
    val name: PlayerInfo,
    var goals: Int,
    var assists: Int,
    var gamesPlayed: Int,
    var penalties: Int,
    var cleanSheet: Int,
    val position: String,
    var lastRanking: Int,
    var ranking: Int,
    var percentage: String? = null
)