package com.sgalera.gaztelubira.data.response

import com.sgalera.gaztelubira.domain.model.PlayerStatsModel

data class PStatsResponse(
    val name: String = "",
    val position: String = "",
    val goals: Int = 0,
    val assists: Int = 0,
    val penalties: Int = 0,
    val cleanSheet: Int = 0,
    val gamesPlayed: Int = 0,
    val ranking: Int = 1,
    val lastRanking: Int = 1
) {
    fun toDomain(): PlayerStatsModel {
        return PlayerStatsModel(
            name = name,
            position = position,
            goals = goals,
            assists = assists,
            penalties = penalties,
            cleanSheet = cleanSheet,
            gamesPlayed = gamesPlayed,
            ranking = ranking,
            lastRanking = lastRanking,
            proportion = (goals + assists + penalties + cleanSheet) / gamesPlayed.toFloat()
        )
    }
}