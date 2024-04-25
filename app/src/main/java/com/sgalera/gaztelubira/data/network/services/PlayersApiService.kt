package com.sgalera.gaztelubira.data.network.services

import com.sgalera.gaztelubira.data.network.firebase.FirebaseClient
import com.sgalera.gaztelubira.data.response.PlayerStatsResponse
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

class PlayersApiService @Inject constructor(private val firebase: FirebaseClient) {

    companion object {
        const val PLAYER_STATS = "players_stats"
        const val PLAYER_TEST = "players_test"
    }

    suspend fun getPlayerStats(playerName: String): PlayerStats? {
        val document =
            firebase.db.collection(PLAYER_STATS).document(playerName.lowercase(Locale.ROOT)).get()
                .await()
        if (document != null) {
            return document.toObject(PlayerStatsResponse::class.java)!!.toDomain()
        }
        return null
    }

    suspend fun getAllStats(): List<PlayerStats>? = try {
        val collection = firebase.db.collection(PLAYER_STATS).get().await()
        if (collection.isEmpty) {
            null
        } else {
            collection.documents.map {
                it.toObject(PlayerStatsResponse::class.java)!!.toDomain()
            }
        }
    } catch (e: Exception) {
        null
    }

    suspend fun insertPlayerStats(playerStats: PlayerStats) {
        firebase.db.collection(PLAYER_TEST).document(playerStats.name.name.lowercase(Locale.ROOT))
            .set(playerStats.toMap()).await()
    }
}
