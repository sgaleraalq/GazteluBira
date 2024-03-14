package com.sgalera.gaztelubira.data.network.services

import com.sgalera.gaztelubira.data.network.firebase.FirebaseClient
import com.sgalera.gaztelubira.data.response.PlayerStatsResponse
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlayersApiService @Inject constructor(private val firebase: FirebaseClient) {

    companion object {
        const val PLAYER_STATS = "player_stats"
    }
    suspend fun getPlayerStats(playerName: String): PlayerStats? {
        val document = firebase.db.collection(PLAYER_STATS).document(playerName).get().await()
        return document?.toObject(PlayerStatsResponse::class.java)?.toDomain()
    }

}
