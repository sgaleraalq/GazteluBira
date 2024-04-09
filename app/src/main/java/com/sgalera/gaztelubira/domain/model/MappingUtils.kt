package com.sgalera.gaztelubira.domain.model

import com.sgalera.gaztelubira.domain.model.Team.*

object MappingUtils {
    fun mapTeam(team: String): Team {
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
            "Tingla2 Legends" -> Tingla2Legends
            else -> GazteluBira
        }
    }
}