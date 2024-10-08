package com.sgalera.gaztelubira.domain.repository

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import kotlinx.coroutines.flow.StateFlow

interface PlayersRepository {

    val playersList: StateFlow<List<PlayerModel?>>
    val playersStats: StateFlow<List<PlayerStatsModel?>>

    suspend fun getPlayers(year: String): Boolean

    suspend fun getPlayersStats(year: String): Boolean

    suspend fun getPlayerStats(playerName: String, year: String): PlayerStatsModel?

    suspend fun getPlayerModel(reference: DocumentReference): PlayerModel?

    suspend fun insertPlayersStats(year: String, updatedPlayersStats: List<PlayerStatsModel?>): Boolean

}