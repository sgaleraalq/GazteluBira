package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.domain.model.PlayerStatsModel
import java.text.DecimalFormat

data class PlayerStatsResponse(
    val reference: DocumentReference? = null,
    val goals: Int = 0,
    val assists: Int = 0,
    val ranking: Int = 0,
    @get:PropertyName("last_ranking") @set:PropertyName("last_ranking") var lastRanking: Int = 0,
    @get:PropertyName("penalties") @set: PropertyName("penalties") var penalties: Int = 0,
    @get: PropertyName("clean_sheet") @set: PropertyName("clean_sheet") var cleanSheet: Int = 0,
    @get: PropertyName("games_played") @set:PropertyName("games_played") var gamesPlayed: Int = 0,
) {
    fun toDomain(): PlayerStatsModel {
        return PlayerStatsModel(
            reference = reference,
            goals = goals,
            assists = assists,
            gamesPlayed = gamesPlayed,
            penalties = penalties,
            cleanSheet = cleanSheet,
            lastRanking = lastRanking,
            ranking = ranking,
            percentage = getPercentage(this)
        )
    }

    private fun getPercentage(player: PlayerStatsResponse): String {
        val total = player.goals + player.assists + player.cleanSheet + player.penalties
        val gamesPlayed = player.gamesPlayed.toFloat()
        return if (gamesPlayed != 0f) {
            val percentage = (total.toFloat() / gamesPlayed)
            DecimalFormat("#.##").format(percentage)
        } else {
            "0"
        }
    }
}
