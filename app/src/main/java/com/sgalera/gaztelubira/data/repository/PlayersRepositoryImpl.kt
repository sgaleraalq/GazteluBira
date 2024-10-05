package com.sgalera.gaztelubira.data.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sgalera.gaztelubira.core.Constants.INFO
import com.sgalera.gaztelubira.core.Constants.PLAYERS
import com.sgalera.gaztelubira.core.Constants.STATS
import com.sgalera.gaztelubira.data.response.players.PlayerResponse
import com.sgalera.gaztelubira.data.response.players.PlayerStatsResponse
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume

class PlayersRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PlayersRepository {

    private val _playersList = MutableStateFlow<List<PlayerModel?>>(emptyList())
    override val playersList: StateFlow<List<PlayerModel?>> get() = _playersList

    private val _playersStats = MutableStateFlow<List<PlayerStatsModel?>>(emptyList())
    override val playersStats: StateFlow<List<PlayerStatsModel?>> get() = _playersStats

    override suspend fun getPlayers(year: String): Boolean {
        return try {
            val querySnapshot = Tasks.await(firestore.collection(PLAYERS).document(year).collection(INFO).get())
            _playersList.value = querySnapshot.toObjects(PlayerResponse::class.java).map { it.toDomain() }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getPlayersStats(year: String): Boolean {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PLAYERS).document(year).collection(STATS).get()
                .addOnSuccessListener { querySnapshot ->
                    _playersStats.value = querySnapshot.toObjects(PlayerStatsResponse::class.java).map { it.toDomain() }
                    cancellableContinuation.resume(true)
                }
                .addOnFailureListener { cancellableContinuation.resume(false) }
        }
    }

    override suspend fun getPlayerStats(playerName: String, year: String): PlayerStatsModel? {
        return firestore.collection(PLAYERS).document(year).collection(STATS)
            .document(playerName.lowercase())
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
}