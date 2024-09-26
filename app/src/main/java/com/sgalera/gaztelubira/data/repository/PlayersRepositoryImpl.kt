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
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayerModelUseCase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.*
import kotlin.coroutines.resume

class PlayersRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PlayersRepository {

    override suspend fun getPlayers() {

    }

    override suspend fun getPlayerModel(reference: DocumentReference?): PlayerModel? {
        return if (reference != null) {
            suspendCancellableCoroutine { cancellableContinuation ->
                firestore.document(reference.path).get()
                    .addOnSuccessListener {
                        cancellableContinuation.resume(
                            it.toObject(
                                PlayerResponse::class.java
                            )?.toDomain()
                        )
                    }
                    .addOnFailureListener { cancellableContinuation.resume(null) }
            }
        } else {
            null
        }
    }

    // Maps Player Stats List Response from Firebase to its Domain Model and also the Player Model inside every Player Stat
    override suspend fun getPlayersStats(
        year: String,
        getPlayerModelUseCase: GetPlayerModelUseCase
    ): List<PlayerStatsModel>? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PLAYERS).document(year).collection(STATS).get()
                .addOnSuccessListener { querySnapshot ->
                    cancellableContinuation.resumeWith(
                        Result.runCatching {
                            runBlocking {
                                querySnapshot.toObjects(PlayerStatsResponse::class.java)
                                    .map { playerStatsResponse ->
                                        playerStatsResponse.toDomain(getPlayerModelUseCase)
                                    }
                            }
                        })
                }
                .addOnFailureListener {
                    cancellableContinuation.resume(null)
                }
        }
    }
}