package com.sgalera.gaztelubira.domain.model

import com.google.firebase.firestore.DocumentReference

data class MatchModel (
    val id: Int,
    val journey: Int,
    val match: String,
    val homeGoals: Int,
    val awayGoals: Int,
    val homeTeam: DocumentReference?,
    val awayTeam: DocumentReference?,
    var localTeam: TeamModel? = null,
    var visitorTeam: TeamModel? = null
)