package com.sgalera.gaztelubira.domain.repository

import com.sgalera.gaztelubira.domain.model.PlayerStatsModel
import com.sgalera.gaztelubira.domain.model.players.PlayerStats

interface PlayersRepository {

    suspend fun getPlayers()

    suspend fun getPlayersStats(year: String): List<PlayerStatsModel>?

}