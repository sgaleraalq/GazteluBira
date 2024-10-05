package com.sgalera.gaztelubira.data.response.matches

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.domain.model.matches.MatchStatsModel
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.teams.TeamModel

data class MatchStatsResponse (
    val match: String = "",
    val journey: Int = 0,
    @get:PropertyName("home_team") @set:PropertyName("home_team") var homeTeamRef: DocumentReference? = null,
    @get:PropertyName("away_team") @set:PropertyName("away_team") var awayTeamRef: DocumentReference? = null,
    @get:PropertyName("home_goals") @set:PropertyName("home_goals") var homeGoals: Int = 0,
    @get:PropertyName("away_goals") @set:PropertyName("away_goals") var awayGoals: Int = 0,
    val scorers: List<DocumentReference> = emptyList(),
    val assistants: List<DocumentReference> = emptyList(),
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
    val bench: List<DocumentReference?> = emptyList()
) {
    fun toDomain(playersRef: List<PlayerModel?>, teamsRef: List<TeamModel?>): MatchStatsModel {
        return MatchStatsModel(
            match = match,
            journey = journey,
            homeTeam = teamsRef.find { it?.ownReference == homeTeamRef },
            awayTeam = teamsRef.find { it?.ownReference == awayTeamRef },
            homeGoals = homeGoals,
            awayGoals = awayGoals,
            scorers = scorers.map { playersRef.find { player -> player?.ownReference == it } },
            assistants = assistants.map { playersRef.find { player -> player?.ownReference == it } },
            starters = starters.mapValues { playersRef.find { player -> player?.ownReference == it.value } },
            bench = bench.map { playersRef.find { player -> player?.ownReference == it } }
        )
    }
}