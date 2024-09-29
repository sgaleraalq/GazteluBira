package com.sgalera.gaztelubira.domain.repository

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.model.MatchModel
import com.sgalera.gaztelubira.domain.model.MatchStatsModel
import com.sgalera.gaztelubira.domain.model.PlayerModel
import com.sgalera.gaztelubira.domain.model.TeamModel

interface MatchesRepository {

    suspend fun getTeam(teamReference: DocumentReference?): TeamModel?

    suspend fun getMatches(year: String): List<MatchModel>?

    suspend fun getMatchStats(
        id: String,
        year: String,
        playersRef: List<PlayerModel?>,
        teamsRef: List<TeamModel?>
    ): MatchStatsModel?

}
