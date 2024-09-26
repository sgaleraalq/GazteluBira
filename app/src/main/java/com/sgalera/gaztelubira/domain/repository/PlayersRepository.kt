package com.sgalera.gaztelubira.domain.repository

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.model.PlayerModel
import com.sgalera.gaztelubira.domain.model.PlayerStatsModel

interface PlayersRepository {

    suspend fun getPlayers()

    suspend fun getPlayerModel(reference: DocumentReference): PlayerModel?

    suspend fun getPlayersStats(year: String): List<PlayerStatsModel>?

}