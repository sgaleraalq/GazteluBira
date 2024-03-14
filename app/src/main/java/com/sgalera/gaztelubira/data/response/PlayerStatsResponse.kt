package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo.*
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import java.util.Locale

data class PlayerStatsResponse(
    val name: String = "",
    val goals: Int = 0,
    val assists: Int = 0,
    val position: String = "",
    @get:PropertyName("big_mistakes") @set: PropertyName("big_mistakes") var bigMistakes: Int = 0,
    @get: PropertyName("clean_sheet") @set: PropertyName("clean_sheet") var cleanSheet: Int = 0,
    @get: PropertyName("games_played") @set:PropertyName("games_played") var gamesPlayed: Int = 0,
) {
    fun toDomain(): PlayerStats {
        return PlayerStats(
            name = mapName(name),
            goals = goals,
            assists = assists,
            gamesPlayed = gamesPlayed,
            bigMistakes = bigMistakes,
            cleanSheet = cleanSheet,
            position = position
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

}
