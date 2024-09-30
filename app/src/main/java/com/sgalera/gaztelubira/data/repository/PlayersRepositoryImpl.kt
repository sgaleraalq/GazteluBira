package com.sgalera.gaztelubira.data.repository

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sgalera.gaztelubira.core.Constants.INFO
import com.sgalera.gaztelubira.core.Constants.PLAYERS
import com.sgalera.gaztelubira.core.Constants.STATS
import com.sgalera.gaztelubira.data.response.PlayerResponse
import com.sgalera.gaztelubira.data.response.PlayerStatsResponse
import com.sgalera.gaztelubira.domain.model.PlayerModel
import com.sgalera.gaztelubira.domain.model.PlayerStatsModel
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume

class PlayersRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PlayersRepository {

    private var _players: List<PlayerModel?> = emptyList()
    override val players: List<PlayerModel?> get() = _players

    // TODO enorme
    override suspend fun getPlayers(year: String): List<PlayerModel?> {
        return try {
            val querySnapshot =
                Tasks.await(firestore.collection(PLAYERS).document(year).collection(INFO).get())
            _players = querySnapshot.toObjects(PlayerResponse::class.java).map { it.toDomain() }
            _players
        } catch (e: Exception) {
            Log.e("PlayersRepository", "Error getting players", e)
            emptyList()
        }
    }

    override suspend fun getPlayerStats(playerName: String, year: String): PlayerStatsModel? {
        return firestore.collection(PLAYERS).document(year).collection(STATS).document(playerName.lowercase())
            .get().await().toObject(PlayerStatsResponse::class.java)?.toDomain()
    }

    override suspend fun getPlayerModel(reference: DocumentReference): PlayerModel? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            reference.get()
                .addOnSuccessListener {
                    cancellableContinuation.resume(
                        it.toObject(PlayerResponse::class.java)?.toDomain()
                    )
                }
                .addOnFailureListener { cancellableContinuation.resume(null) }
        }
    }

    override suspend fun getPlayersStats(year: String): List<PlayerStatsModel>? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PLAYERS).document(year).collection(STATS).get()
                .addOnSuccessListener { querySnapshot ->
                    val playerStatsList = querySnapshot.toObjects(PlayerStatsResponse::class.java)
                        .map { it.toDomain() }
                    cancellableContinuation.resume(playerStatsList)
                }
                .addOnFailureListener {
                    cancellableContinuation.resume(null)
                }
        }
    }
}