package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerStats

data class PlayerStatsResponse(
    val name: PlayerInfo,
    val goals: Int,
    val assists: Int,
    val position: String,
    @get:PropertyName("big_mistakes") @set: PropertyName("big_mistakes") var bigMistakes: Int,
    @get: PropertyName("clean_sheet") @set: PropertyName("clean_sheet") var cleanSheet: Int,
    @get: PropertyName("games_played") @set:PropertyName("games_played") var gamesPlayed: Int,
    var percentage: String? = null
){
    fun toDomain(): PlayerStats{
        return PlayerStats(
            name = name,
            goals = goals,
            assists = assists,
            gamesPlayed = gamesPlayed,
            bigMistakes = bigMistakes,
            cleanSheet = cleanSheet,
            position = position
        )
    }

}
