package com.sgalera.gaztelubira.data.response

import com.google.gson.annotations.SerializedName
import com.sgalera.gaztelubira.domain.model.Team
import com.sgalera.gaztelubira.domain.model.Team.*
import com.sgalera.gaztelubira.domain.model.matches.Match
import com.sgalera.gaztelubira.domain.model.matches.Starters

data class MatchResponse (
    @SerializedName("id") val id: Int,
    @SerializedName("home_team") val homeTeam: String,
    @SerializedName("away_team") val awayTeam: String,
    @SerializedName("home_goals") val homeGoals: Int,
    @SerializedName("away_goals") val awayGoals: Int,
    @SerializedName("scorers") val scorers: List<String>,
    @SerializedName("assitants") val assitants: List<String>,
    @SerializedName("starters") val starters: Starters,
    @SerializedName("bench") val bench: List<String>
){
    fun toDomain() = Match(
        id = id,
        local = mapTeam(homeTeam),
        visitor = mapTeam(awayTeam),
        localGoals = homeGoals,
        visitorGoals = awayGoals,
        scorers = scorers,
        assitants = assitants,
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

