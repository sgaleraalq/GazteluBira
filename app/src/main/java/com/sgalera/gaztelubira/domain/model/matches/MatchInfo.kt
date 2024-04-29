package com.sgalera.gaztelubira.domain.model.matches

data class MatchInfo (
    val id: Int,
    val match: String,
    val homeTeam: String,
    val awayTeam: String,
    val homeGoals: Int,
    val awayGoals: Int,
    val journey: String
) {
    fun toDomain(): HashMap<String, Any> {
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