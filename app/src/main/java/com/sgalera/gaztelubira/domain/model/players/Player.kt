package com.sgalera.gaztelubira.domain.model.players

data class Player(
    val name: String,
    val goals: Int,
    val assists: Int,
    val gamesPlayed: Int,
    val bigMistakes: Int,
    val cleanSheet: Int,
    val positions: List<Positions>
)