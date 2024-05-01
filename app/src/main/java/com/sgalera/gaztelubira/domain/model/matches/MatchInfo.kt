package com.sgalera.gaztelubira.domain.model.matches

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.model.TeamInformation

data class MatchInfo (
    val id: Int,
    val match: String,
    val homeTeam: TeamInformation? = null,
    val awayTeam: TeamInformation? = null,
    val homeGoals: Int,
    val awayGoals: Int,
    val journey: String
) {
    fun toDomain(): HashMap<String, Any?> {
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