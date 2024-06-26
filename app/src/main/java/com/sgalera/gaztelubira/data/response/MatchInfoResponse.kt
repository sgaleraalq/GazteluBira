package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.data.network.services.TeamsApiService
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo

data class MatchInfoResponse(
    val id: Int = 0,
    val match: String = "",
    @get:PropertyName("home_team") @set:PropertyName("home_team") var homeTeam: DocumentReference? = null,
    @get:PropertyName("away_team") @set:PropertyName("away_team") var awayTeam: DocumentReference? = null,
    @get:PropertyName("home_goals") @set:PropertyName("home_goals") var homeGoals: Int = 0,
    @get:PropertyName("away_goals") @set:PropertyName("away_goals") var awayGoals: Int = 0,
    val journey: String = ""
) {
    suspend fun toDomain(teamsApiService: TeamsApiService): MatchInfo {
        val homeTeamInfo = homeTeam?.let { teamsApiService.getTeam(it) }
        val awayTeamInfo = awayTeam?.let { teamsApiService.getTeam(it) }

        return MatchInfo(
            id = id,
            match = match,
            homeTeam = homeTeamInfo,
            awayTeam = awayTeamInfo,
            homeGoals = homeGoals,
            awayGoals = awayGoals,
            journey = journey
        )
    }

    fun toResponse(): HashMap<String, Any?> {
        return hashMapOf(
            "id" to id,
            "match" to match,
            "home_team" to homeTeam,
            "away_team" to awayTeam,
            "home_goals" to homeGoals,
            "away_goals" to awayGoals,
            "journey" to journey
        )
    }
}