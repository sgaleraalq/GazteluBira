package com.sgalera.gaztelubira.data.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sgalera.gaztelubira.core.Constants.GAMES
import com.sgalera.gaztelubira.core.Constants.MATCHES
import com.sgalera.gaztelubira.core.Constants.STATS
import com.sgalera.gaztelubira.core.Constants.TEAMS
import com.sgalera.gaztelubira.data.response.MatchR
import com.sgalera.gaztelubira.data.response.TeamResponse
import com.sgalera.gaztelubira.domain.model.MatchModel
import com.sgalera.gaztelubira.domain.model.TeamModel
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
                        snapshot.toObjects(MatchR::class.java).map { it.toDomain() }
                    )
                }
                .addOnFailureListener { cancellableContinuation.resume(null) }
        }
    }

}