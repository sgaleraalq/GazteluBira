package com.sgalera.gaztelubira.domain.model.players

import com.google.firebase.firestore.DocumentReference

data class PlayerStatsModel(
    var information: PlayerModel? = null,
    val reference: DocumentReference?,
    val goals: Int,
    val assists: Int,
    val penalties: Int,
    val cleanSheet: Int,
    val gamesPlayed: Int,
    val ranking: Int,
    val lastRanking: Int,
    val percentage: String
)