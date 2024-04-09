package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo.*
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import java.text.DecimalFormat

data class PlayerStatsResponse(
    val name: String = "",
    val goals: Int = 0,
    val assists: Int = 0,
    val position: String = "",
    val ranking: Int = 0,
    @get:PropertyName("last_ranking") @set:PropertyName("last_ranking") var lastRanking: Int = 0,
    @get:PropertyName("penalties") @set: PropertyName("penalties") var penalties: Int = 0,
    @get: PropertyName("clean_sheet") @set: PropertyName("clean_sheet") var cleanSheet: Int = 0,
    @get: PropertyName("games_played") @set:PropertyName("games_played") var gamesPlayed: Int = 0,
) {
    fun toDomain(): PlayerStats {
        return PlayerStats(
            name = mapName(name),
            goals = goals,
            assists = assists,
            gamesPlayed = gamesPlayed,
            penalties = penalties,
            cleanSheet = cleanSheet,
            position = position,
            lastRanking = lastRanking,
            ranking = ranking,
            percentage = getPercentage(this)
        )
    }

    private fun mapName(name: String): PlayerInfo {
        return when (name) {
            "Pedro" -> Pedro
            "Jon" -> Jon
            "Asier" -> Asier
            "Manu" -> Manu
            "Xabi" -> Xabi
            "Oso" -> Oso
            "Diego" -> Diego
            "Mikel" -> Mikel
            "Gorka" -> Gorka
            "Arrondo" -> Arrondo
            "Dani" -> Dani
            "Nando" -> Nando
            "Haaland" -> Haaland
            "David" -> David
            "Aaron" -> Aaron
            "Mugueta" -> Mugueta
            "Fran" -> Fran
            "Iker" -> Iker
            "Larra" -> Larra
            "Unai" -> Unai
            "Madariaga" -> Madariaga
            "Bryant" -> Bryant
            "Roson" -> Roson
            else -> Emilio
        }
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
