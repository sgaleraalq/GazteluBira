package com.sgalera.gaztelubira.data.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sgalera.gaztelubira.core.Constants.GAMES
import com.sgalera.gaztelubira.core.Constants.MATCHES
import com.sgalera.gaztelubira.core.Constants.STATS
import com.sgalera.gaztelubira.data.response.matches.MatchResponse
import com.sgalera.gaztelubira.data.response.matches.MatchStatsResponse
import com.sgalera.gaztelubira.data.response.teams.TeamResponse
import com.sgalera.gaztelubira.domain.model.matches.MatchModel
import com.sgalera.gaztelubira.domain.model.matches.MatchStatsModel
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.teams.TeamModel
import com.sgalera.gaztelubira.domain.repository.MatchesRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume

class MatchesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MatchesRepository {
    // TODO check if we can inject sharedPreferences

    override suspend fun getTeam(teamReference: DocumentReference?): TeamModel? {
        if (teamReference == null) return null
        return teamReference.get().await().toObject(TeamResponse::class.java)?.toDomain()
    }

    override suspend fun getMatches(year: String): List<MatchModel>? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(MATCHES).document(year).collection(GAMES).get()
                .addOnSuccessListener { snapshot ->
                    cancellableContinuation.resume(
                        snapshot.toObjects(MatchResponse::class.java).map { it.toDomain() }
                    )
                }
                .addOnFailureListener { cancellableContinuation.resume(null) }
        }
    }

    override suspend fun getMatchStats(
        id: String,
        year: String,
        playersRef: List<PlayerModel?>,
        teamsRef: List<TeamModel?>
    ): MatchStatsModel? {
        return firestore.collection(MATCHES)
            .document(year).collection(STATS)
            .document(id).get().await()
            .toObject(MatchStatsResponse::class.java)?.toDomain(playersRef, teamsRef)
    }

    override suspend fun insertGame(id: String, year: String, matchModel: MatchModel): Boolean {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(MATCHES).document(year).collection(GAMES).document(id).set(matchModel)
                .addOnSuccessListener { cancellableContinuation.resume(true) }
                .addOnFailureListener { cancellableContinuation.resume(false) }
        }
    }

    override suspend fun insertMatchStats(id: String, year: String, matchStats: MatchStatsModel): Boolean {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(MATCHES).document(year).collection(STATS).document(id).set(matchStats)
                .addOnSuccessListener { cancellableContinuation.resume(true) }
                .addOnFailureListener { cancellableContinuation.resume(false) }
        }
    }
}