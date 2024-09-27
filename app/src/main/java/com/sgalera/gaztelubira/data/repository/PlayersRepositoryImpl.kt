package com.sgalera.gaztelubira.data.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sgalera.gaztelubira.core.Constants.PLAYERS
import com.sgalera.gaztelubira.core.Constants.STATS
import com.sgalera.gaztelubira.data.response.PlayerResponse
import com.sgalera.gaztelubira.data.response.PlayerStatsResponse
import com.sgalera.gaztelubira.domain.model.PlayerModel
import com.sgalera.gaztelubira.domain.model.PlayerStatsModel
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class PlayersRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PlayersRepository {

    override suspend fun getPlayers() {

    }

    override suspend fun getPlayerModel(reference: DocumentReference): PlayerModel? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            reference.get()
                .addOnSuccessListener { cancellableContinuation.resume(it.toObject(PlayerResponse::class.java)?.toDomain()) }
                .addOnFailureListener { cancellableContinuation.resume(null) }
        }
    }

    override suspend fun getPlayersStats(year: String): List<PlayerStatsModel>? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PLAYERS).document(year).collection(STATS).get()
                .addOnSuccessListener { querySnapshot ->
                    val playerStatsList = querySnapshot.toObjects(PlayerStatsResponse::class.java).map { it.toDomain() }
                    cancellableContinuation.resume(playerStatsList)
                }
                .addOnFailureListener {
                    cancellableContinuation.resume(null)
                }
        }
    }
}