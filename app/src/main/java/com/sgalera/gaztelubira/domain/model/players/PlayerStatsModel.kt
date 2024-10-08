package com.sgalera.gaztelubira.domain.model.players

import com.google.firebase.firestore.DocumentReference

data class PlayerStatsModel(
    var information: PlayerModel? = null,
    val reference: DocumentReference?,
    var goals: Int,
    var assists: Int,
    var penalties: Int,
    var cleanSheet: Int,
    var gamesPlayed: Int,
    var ranking: Int,
    var lastRanking: Int,
    var percentage: String,
    var sortedPercentage: Float = 0f
)