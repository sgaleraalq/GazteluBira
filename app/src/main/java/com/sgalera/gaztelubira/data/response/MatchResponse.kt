package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.model.players.PlayerMapper.mapPlayerInformation
import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.data.network.services.TeamsApiService
import com.sgalera.gaztelubira.domain.model.MatchModel
import com.sgalera.gaztelubira.domain.model.matches.Match
import kotlinx.coroutines.runBlocking

data class MatchResponse(
    val id: Int = 0,
    val match: String = "",
    val scorers: List<String> = emptyList(),
    val assistants: List<String> = emptyList(),
    val starters: Map<String, String> = emptyMap(),
    val bench: List<String> = emptyList(),
    @get:PropertyName("home_team") @set:PropertyName("home_team") var homeTeam: DocumentReference? = null,
    @get:PropertyName("away_team") @set:PropertyName("away_team") var awayTeam: DocumentReference? = null,
    @get:PropertyName("home_goals") @set:PropertyName("home_goals") var homeGoals: Int = 0,
    @get:PropertyName("away_goals") @set: PropertyName("away_goals") var awayGoals: Int = 0
) {
    suspend fun toDomain(teamsApiService: TeamsApiService): Match {
        val homeTeamInfo = homeTeam?.let { teamsApiService.getTeam(it) }
        val awayTeamInfo = awayTeam?.let { teamsApiService.getTeam(it) }

        return Match(
            id = id,
            match = match,
            local = homeTeamInfo,
            visitor = awayTeamInfo,
            localGoals = homeGoals,
            visitorGoals = awayGoals,
            scorers = scorers,
            assistants = assistants,
            starters = starters.mapValues { runBlocking { mapPlayerInformation(it.value) } },
            bench = bench.map { runBlocking { mapPlayerInformation(it) } }
        )
    }
}