package com.sgalera.gaztelubira.domain.model.matches

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.model.teams.TeamModel

data class MatchModel (
    val id: Int = 0,
    val journey: Int = -1,
    var match: String? = null,
    val homeGoals: Int = -1,
    val awayGoals: Int = -1,
    var homeTeam: DocumentReference? = null,
    var awayTeam: DocumentReference? = null,
    var localTeam: TeamModel? = null,
    var visitorTeam: TeamModel? = null
)