package com.sgalera.gaztelubira.data.response.matches

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.domain.model.matches.MatchModel

data class MatchResponse (
    val id: Int = 0,
    val journey: Int = 0,
    val match: String = "",
    @get:PropertyName("home_goals") @set:PropertyName("home_goals") var homeGoals: Int = 0,
    @get:PropertyName("away_goals") @set:PropertyName("away_goals") var awayGoals: Int = 0,
    @get:PropertyName("home_team") @set:PropertyName("home_team") var homeTeam: DocumentReference? = null,
    @get:PropertyName("away_team") @set:PropertyName("away_team") var awayTeam: DocumentReference? = null
) {
    fun toDomain(): MatchModel {
        return MatchModel(
            id = id,
            journey = journey,
            match = match,
            homeGoals = homeGoals,
            awayGoals = awayGoals,
            homeTeam = homeTeam,
            awayTeam = awayTeam
        )
    }
}