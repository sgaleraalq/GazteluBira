package com.sgalera.gaztelubira.data.provider

import com.sgalera.gaztelubira.data.network.services.PlayersApiService
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import javax.inject.Inject

class PlayersProvider @Inject constructor(private val playersApiService: PlayersApiService) {
    suspend fun getPlayerStats(playerName: String): PlayerStats? {
        return playersApiService.getPlayerStats(playerName)
    }

    suspend fun getAllStats(): List<PlayerStats>? {
        return playersApiService.getAllStats()
    }

    suspend fun insertPlayerStats(playerStats: PlayerStats) {
        playersApiService.insertPlayerStats(playerStats)
    }
}
