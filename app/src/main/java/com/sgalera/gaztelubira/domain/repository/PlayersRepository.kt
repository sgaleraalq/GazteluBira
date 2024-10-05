package com.sgalera.gaztelubira.domain.repository

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel

interface PlayersRepository {

    val players: List<PlayerModel?>

    suspend fun getPlayers(year: String): List<PlayerModel?> // TODO

    suspend fun getPlayerStats(playerName: String, year: String): PlayerStatsModel?

    suspend fun getPlayerModel(reference: DocumentReference): PlayerModel?

    suspend fun getPlayersStats(year: String): List<PlayerStatsModel>?

}