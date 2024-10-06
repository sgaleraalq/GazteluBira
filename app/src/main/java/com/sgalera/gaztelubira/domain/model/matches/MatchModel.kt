package com.sgalera.gaztelubira.domain.model.matches

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.model.teams.TeamModel

data class MatchModel (
    val id: Int? = null,
    val journey: Int? = null,
    val match: String? = null,
    val homeGoals: Int? = null,
    val awayGoals: Int? = null,
    val homeTeam: DocumentReference? = null,
    val awayTeam: DocumentReference? = null,
    var localTeam: TeamModel? = null,
    var visitorTeam: TeamModel? = null
)