package com.sgalera.gaztelubira.domain.model.matches

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.model.teams.TeamModel

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