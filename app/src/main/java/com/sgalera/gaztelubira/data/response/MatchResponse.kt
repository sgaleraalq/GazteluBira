package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import com.sgalera.gaztelubira.domain.model.Team
import com.sgalera.gaztelubira.domain.model.Team.*
import com.sgalera.gaztelubira.domain.model.matches.Match
import com.sgalera.gaztelubira.domain.model.matches.Starters

data class MatchResponse (
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
){
    fun toDomain() = Match(
        id = id,
        match = match,
        local = mapTeam(homeTeam),
        visitor = mapTeam(awayTeam),
        localGoals = homeGoals,
        visitorGoals = awayGoals,
        scorers = scorers,
        assistants = assistants,
        starters = starters,
        bench = bench
    )

    private fun mapTeam(team: String): Team {
        return when(team){
            "Gaztelu Bira"  -> GazteluBira
            "Anaitasuna"    -> Anaitasuna
            "Arsenal"       -> Arsenal
            "Aterbea"       -> Aterbea
            "ESIC Gazteak"  -> EsicGazteak
            "Esmeraldeños"  -> Esmeraldenos
            "Garre"         -> Garre
            "Iturrama"      -> Iturrama
            "IZN"           -> Izn
            "La Unica"      -> LaUnica
            "Peña School"   -> PenaSchool
            "San Cristobal" -> SanCristobal
            "Lezkairu"      -> Lezkairu
            else -> Tingla2Legens
        }
    }
}

