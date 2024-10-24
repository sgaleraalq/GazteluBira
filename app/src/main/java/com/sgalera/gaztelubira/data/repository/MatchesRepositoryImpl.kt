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
        val journey = if (matchModel.match == "liga") matchModel.journey else 0
        val data = mapOf(
            "id" to matchModel.id,
            "journey" to journey,
            "match" to matchModel.match,
            "home_goals" to matchModel.homeGoals,
            "home_team" to matchModel.homeTeam,
            "away_goals" to matchModel.awayGoals,
            "away_team" to matchModel.awayTeam
        )
        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(MATCHES).document(year).collection(GAMES).document(id).set(data)
                .addOnSuccessListener { cancellableContinuation.resume(true) }
                .addOnFailureListener { cancellableContinuation.resume(false) }
        }
    }

    override suspend fun insertMatchStats(id: String, year: String, matchStats: MatchStatsModel): Boolean {
        val journey = if (matchStats.match == "liga") matchStats.journey else 0
        val matchStarters = matchStats.starters.mapValues { it.value?.ownReference }
        val data = mapOf(
            "id" to matchStats.id,
            "journey" to journey,
            "match" to matchStats.match,
            "home_goals" to matchStats.homeGoals,
            "home_team" to matchStats.homeTeam?.ownReference,
            "away_goals" to matchStats.awayGoals,
            "away_team" to matchStats.awayTeam?.ownReference,
            "starters" to matchStarters,
            "bench" to matchStats.bench.map { it?.ownReference },
            "scorers" to matchStats.scorers.map { it?.ownReference },
            "assistants" to matchStats.assistants.map { it?.ownReference },
            "penalties" to matchStats.penalties.map { it?.ownReference },
        )
        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(MATCHES).document(year).collection(STATS).document(id).set(data)
                .addOnSuccessListener { cancellableContinuation.resume(true) }
                .addOnFailureListener { cancellableContinuation.resume(false) }
        }
    }
}