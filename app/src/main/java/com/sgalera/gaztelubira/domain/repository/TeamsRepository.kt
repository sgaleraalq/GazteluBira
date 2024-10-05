package com.sgalera.gaztelubira.domain.repository

import com.sgalera.gaztelubira.domain.model.teams.TeamModel

interface TeamsRepository {

    suspend fun getTeams(year: String): List<TeamModel>

}
