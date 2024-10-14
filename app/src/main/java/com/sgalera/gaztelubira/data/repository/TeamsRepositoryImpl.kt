package com.sgalera.gaztelubira.data.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.sgalera.gaztelubira.core.Constants.TEAMS
import com.sgalera.gaztelubira.data.response.teams.TeamResponse
import com.sgalera.gaztelubira.domain.model.teams.TeamModel
import com.sgalera.gaztelubira.domain.repository.TeamsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class TeamsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): TeamsRepository {

    private val _teamsList = MutableStateFlow<List<TeamModel?>>(emptyList())
    override val teamsList: StateFlow<List<TeamModel?>> get() = _teamsList

    override suspend fun getTeams(year: String): Boolean {
        return try {
            val querySnapshot = Tasks.await(firestore.collection(TEAMS).document(year).collection(TEAMS).get())
            _teamsList.value = querySnapshot.toObjects(TeamResponse::class.java).map { it.toDomain() }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getSeasons(): List<String>? {
        return suspendCancellableCoroutine { continuation ->
            firestore.collection(TEAMS).get()
                .addOnSuccessListener {continuation.resumeWith(Result.success(it.documents.map { doc -> doc.id })) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }
}