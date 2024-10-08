package com.sgalera.gaztelubira.domain.repository

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.model.matches.MatchModel
import com.sgalera.gaztelubira.domain.model.matches.MatchStatsModel
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.teams.TeamModel

interface MatchesRepository {

    suspend fun getTeam(teamReference: DocumentReference?): TeamModel?

    suspend fun getMatches(year: String): List<MatchModel>?

    suspend fun getMatchStats(
        id: String,
        year: String,
        playersRef: List<PlayerModel?>,
        teamsRef: List<TeamModel?>
    ): MatchStatsModel?

    suspend fun insertGame(id: String, year: String, matchModel: MatchModel): Boolean

    suspend fun insertMatchStats(id: String, year: String, matchStats: MatchStatsModel): Boolean

}
