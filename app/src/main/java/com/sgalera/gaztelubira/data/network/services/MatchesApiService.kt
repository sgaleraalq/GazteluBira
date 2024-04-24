package com.sgalera.gaztelubira.data.network.services

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.data.network.firebase.FirebaseClient
import com.sgalera.gaztelubira.data.response.MatchInfoResponse
import com.sgalera.gaztelubira.data.response.MatchResponse
import com.sgalera.gaztelubira.domain.model.matches.Match
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CompletableFuture
import javax.inject.Inject

class MatchesApiService @Inject constructor(private val firebase: FirebaseClient) {
    companion object {
        const val MATCHES_INFO = "matches"
        const val MATCHES_STATS = "matches_stats"
    }

    suspend fun getMatchesInfo(): List<MatchInfo>? = try {
        val collection = firebase.db.collection(MATCHES_INFO).get().await()
        if (collection.isEmpty) {
            null
        } else {
            collection.documents.map { it.toObject(MatchInfoResponse::class.java)!!.toDomain() }
        }
    } catch (e: Exception) {
        null
    }

    suspend fun getMatch(id: Int): Match? {
        val document = firebase.db.collection(MATCHES_STATS).document(id.toString()).get().await()
        if (document != null){
            return document.toObject(MatchResponse::class.java)!!.toDomain()
        }
        return null
    }
    suspend fun postGame(match: MatchInfo): Boolean {
        return try {
            firebase.db.collection(MATCHES_INFO).document(match.id.toString()).set(match.toDomain()).await()
            true // Success
        } catch (e: Exception) {
            false // Error
        }
    }

    suspend fun postGameStats(matchStats: MatchResponse): Boolean {
        return try {
            firebase.db.collection(MATCHES_STATS).document(matchStats.id.toString()).set(matchStats).await()
            true // Success
        } catch (e: Exception) {
            false // Error
        }
    }
}

