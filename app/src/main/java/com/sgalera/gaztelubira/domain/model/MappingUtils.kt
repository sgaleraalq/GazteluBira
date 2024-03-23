package com.sgalera.gaztelubira.domain.model

object MappingUtils {
    fun mapTeam(team: String): Team {
        return when (team) {
            "Gaztelu Bira" -> Team.GazteluBira
            "Anaitasuna" -> Team.Anaitasuna
            "Arsenal" -> Team.Arsenal
            "Aterbea" -> Team.Aterbea
            "ESIC Gazteak" -> Team.EsicGazteak
            "Esmeraldeños" -> Team.Esmeraldenos
            "Garre" -> Team.Garre
            "Iturrama" -> Team.Iturrama
            "IZN" -> Team.Izn
            "La Unica" -> Team.LaUnica
            "Peña School" -> Team.PenaSchool
            "San Cristobal" -> Team.SanCristobal
            "Lezkairu" -> Team.Lezkairu
            else -> Team.Tingla2Legends
        }
    }
}