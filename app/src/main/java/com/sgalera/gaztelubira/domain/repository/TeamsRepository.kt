package com.sgalera.gaztelubira.domain.repository

import com.sgalera.gaztelubira.domain.model.teams.TeamModel
import kotlinx.coroutines.flow.StateFlow

interface TeamsRepository {

    val teamsList: StateFlow<List<TeamModel?>>

    suspend fun getTeams(year: String): Boolean

}
