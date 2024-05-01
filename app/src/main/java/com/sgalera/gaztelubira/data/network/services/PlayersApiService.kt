package com.sgalera.gaztelubira.data.network.services

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.data.network.firebase.FirebaseClient
import com.sgalera.gaztelubira.data.response.PlayerInfoResponse
import com.sgalera.gaztelubira.data.response.PlayerStatsResponse
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

class PlayersApiService @Inject constructor(private val firebase: FirebaseClient) {

    companion object {
        const val PLAYER_STATS = "players_stats"
        const val PLAYER_TEST = "players_test"
        const val PLAYERS = "players"
    }

    suspend fun getPlayerStatsByReference(playerReference: DocumentReference): PlayerStats? {
        val document = playerReference.get().await()
        return if (document != null) {
            document.toObject(PlayerStatsResponse::class.java)!!.toDomain()
        } else{
            null
        }
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
        firebase.db.collection(PLAYER_TEST)
            .document(playerStats.information!!.name.lowercase(Locale.ROOT))
            .set(playerStats.toMap()).await()
    }

    suspend fun playerInformation(playerName: String): PlayerInformation? {
        val document =
            firebase.db.collection(PLAYERS).document(playerName.lowercase(Locale.ROOT)).get()
                .await()
        if (document != null) {
            try {
                document.toObject(PlayerInfoResponse::class.java)
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
            return document.toObject(PlayerInfoResponse::class.java)!!.toDomain()
        }
        return null
    }

    // Gets all player information
    suspend fun getAllPlayers(): List<PlayerInformation>? = try {
        val collection = firebase.db.collection(PLAYERS).get().await()
        if (collection.isEmpty) {
            null
        } else {
            collection.documents.map {
                it.toObject(PlayerInfoResponse::class.java)!!.toDomain()
            }
        }
    } catch (e: Exception) {
        null
    }

}
