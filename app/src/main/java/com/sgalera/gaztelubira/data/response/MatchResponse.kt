package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.domain.model.Team
import com.sgalera.gaztelubira.domain.model.Team.*
import com.sgalera.gaztelubira.domain.model.matches.Match
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo.*

data class MatchResponse(
    val id: Int = 0,
    val match: String = "",
    val scorers: List<String> = emptyList(),
    val assistants: List<String> = emptyList(),
    // TODO Starters
    val starters: Map<String, String> = emptyMap(),
    val bench: List<String> = emptyList(),
    @get:PropertyName("home_team") @set:PropertyName("home_team") var homeTeam: String = "",
    @get:PropertyName("away_team") @set:PropertyName("away_team") var awayTeam: String = "",
    @get:PropertyName("home_goals") @set:PropertyName("home_goals") var homeGoals: Int = 0,
    @get:PropertyName("away_goals") @set: PropertyName("away_goals") var awayGoals: Int = 0
) {
    fun toDomain() = Match(
        id = id,
        match = match,
        local = mapTeam(homeTeam),
        visitor = mapTeam(awayTeam),
        localGoals = homeGoals,
        visitorGoals = awayGoals,
        scorers = scorers,
        assistants = assistants,
        starters = starters.mapValues { mapPlayerInfo(it.value) },
        bench = bench.map { mapPlayerInfo(it) }
    )

    private fun mapTeam(team: String): Team {
        return when (team) {
            "Gaztelu Bira" -> GazteluBira
            "Anaitasuna" -> Anaitasuna
            "Arsenal" -> Arsenal
            "Aterbea" -> Aterbea
            "ESIC Gazteak" -> EsicGazteak
            "Esmeraldeños" -> Esmeraldenos
            "Garre" -> Garre
            "Iturrama" -> Iturrama
            "IZN" -> Izn
            "La Unica" -> LaUnica
            "Peña School" -> PenaSchool
            "San Cristobal" -> SanCristobal
            "Lezkairu" -> Lezkairu
            else -> Tingla2Legens
        }
    }

    private fun mapPlayerInfo(player: String): PlayerInfo {
        return when (player) {
            "pedro" -> Pedro
            "jon" -> Jon
            "asier" -> Asier
            "xabi" -> Xabi
            "oso" -> Oso
            "diego" -> Diego
            "mikel" -> Mikel
            "gorka" -> Gorka
            "arrondo" -> Arrondo
            "dani" -> Dani
            "nando" -> Nando
            "haaland" -> Haaland
            "david" -> David
            "aaron" -> Aaron
            "mugueta" -> Mugueta
            "fran" -> Fran
            "iker" -> Iker
            "larra" -> Larra
            "unai" -> Unai
            "manu" -> Manu
            "madariaga" -> Madariaga
            "bryant" -> Bryant
            "roson" -> Roson
            else -> Emilio
        }
    }
}

