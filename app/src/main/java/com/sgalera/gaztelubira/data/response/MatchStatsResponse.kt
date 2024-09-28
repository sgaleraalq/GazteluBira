package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.domain.model.MatchStatsModel
import com.sgalera.gaztelubira.domain.model.TeamModel

data class MatchStatsResponse (
    val match: String = "",
    @get:PropertyName("home_team") @set:PropertyName("home_team") var homeTeam: DocumentReference? = null,
    @get:PropertyName("away_team") @set:PropertyName("away_team") var awayTeam: DocumentReference? = null,
    @get:PropertyName("home_goals") @set:PropertyName("home_goals") var homeGoals: Int = 0,
    @get:PropertyName("away_goals") @set:PropertyName("away_goals") var awayGoals: Int = 0,
    val scorers: List<String> = emptyList(),
    val assistants: List<String> = emptyList(),
    val starters: Map<String, DocumentReference?> = mapOf(
        "goal_keeper" to null,
        "left_back" to null,
        "right_back" to null,
        "left_centre_back" to null,
        "right_centre_back" to null,
        "defensive_mid_fielder" to null,
        "left_mid_fielder" to null,
        "right_mid_fielder" to null,
        "left_striker" to null,
        "right_striker" to null,
        "striker" to null
    ),
    val bench: List<String> = emptyList()
) {
    fun toDomain(): MatchStatsModel {
        return MatchStatsModel(
            match = match,
            homeTeam = TeamModel("", ""),
            awayTeam = TeamModel("", ""),
            homeGoals = homeGoals,
            awayGoals = awayGoals,
            scorers = scorers,
            assistants = assistants,
            starters = emptyMap(),
            bench = emptyList()
        )
    }
}