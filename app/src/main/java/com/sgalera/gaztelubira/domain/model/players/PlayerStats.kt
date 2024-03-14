package com.sgalera.gaztelubira.domain.model.players

data class PlayerStats(
    val name: PlayerInfo,
    val goals: Int,
    val assists: Int,
    val gamesPlayed: Int,
    val bigMistakes: Int,
    val cleanSheet: Int,
    val position: String,
    var percentage: String? = null
)