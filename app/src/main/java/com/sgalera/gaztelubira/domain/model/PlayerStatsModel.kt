package com.sgalera.gaztelubira.domain.model

data class PlayerStatsModel(
    val name: String,
    val position: String,
    val goals: Int,
    val assists: Int,
    val penalties: Int,
    val cleanSheet: Int,
    val gamesPlayed: Int,
    val ranking: Int,
    val lastRanking: Int,
    val proportion: Float = 0.0f
)