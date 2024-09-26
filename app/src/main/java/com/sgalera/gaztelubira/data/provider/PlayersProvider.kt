package com.sgalera.gaztelubira.data.provider

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.data.network.services.PlayersApiService
import com.sgalera.gaztelubira.domain.model.PlayerStatsModel
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import javax.inject.Inject

class PlayersProvider @Inject constructor(private val playersApiService: PlayersApiService) {

    fun getReferenceFromString(reference: String): DocumentReference? {
        return playersApiService.getReferenceFromString(reference)
    }

//    suspend fun getPlayerStatsByReference(playerReference: DocumentReference): PlayerStatsModel? {
//        return playersApiService.getPlayerStatsByReference(playerReference)
//    }
//
//    suspend fun getAllStats(): List<PlayerStatsModel>? {
//        return playersApiService.getAllStats()
//    }

    suspend fun insertPlayerStats(playerStats: PlayerStats) {
        playersApiService.insertPlayerStats(playerStats)
    }

    suspend fun getAllPlayers(): MutableList<PlayerInformation>? {
        return playersApiService.getAllPlayers() as MutableList<PlayerInformation>?
    }

}
