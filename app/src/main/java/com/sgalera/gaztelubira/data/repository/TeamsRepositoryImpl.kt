package com.sgalera.gaztelubira.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sgalera.gaztelubira.core.Constants.TEAMS
import com.sgalera.gaztelubira.data.response.teams.TeamResponse
import com.sgalera.gaztelubira.domain.repository.TeamsRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TeamsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): TeamsRepository {

    override suspend fun getTeams(year: String)= firestore.collection(TEAMS).document(year).collection(TEAMS).get()
            .await().toObjects(TeamResponse::class.java).map { it.toDomain() }

}