package com.sgalera.gaztelubira.domain.repository

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.model.PlayerModel
import com.sgalera.gaztelubira.domain.model.PlayerStatsModel

interface PlayersRepository {

    val players: List<PlayerModel?>

    suspend fun getPlayers(year: String): List<PlayerModel?> // TODO

    suspend fun getPlayerModel(reference: DocumentReference): PlayerModel?

    suspend fun getPlayersStats(year: String): List<PlayerStatsModel>?

}