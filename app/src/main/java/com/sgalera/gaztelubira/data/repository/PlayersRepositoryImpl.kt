package com.sgalera.gaztelubira.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.sgalera.gaztelubira.core.Constants.PLAYERS
import com.sgalera.gaztelubira.core.Constants.STATS
import com.sgalera.gaztelubira.data.response.PStatsResponse
import com.sgalera.gaztelubira.domain.model.PlayerStatsModel
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class PlayersRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): PlayersRepository {
    override suspend fun getPlayers() {

    }

    override suspend fun getPlayersStats(year: String): List<PlayerStatsModel>? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PLAYERS).document(year).collection(STATS).get()
                .addOnSuccessListener { querySnapshot ->
                    Log.i("StatsFragment", "getPlayersStats: ${querySnapshot.documents}")
                    Log.i("StatsFragment", "year: $year")
                    val playerStatsList = querySnapshot.toObjects(PStatsResponse::class.java).map { it.toDomain() }
                    cancellableContinuation.resume(playerStatsList)
                }
                .addOnFailureListener {
                    cancellableContinuation.resume(null)
                }
        }
    }
}