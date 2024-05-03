package com.sgalera.gaztelubira.data.network.services

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.data.network.firebase.FirebaseClient
import com.sgalera.gaztelubira.domain.model.TeamInformation
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TeamsApiService @Inject constructor(private val firebase: FirebaseClient) {
    companion object {
        const val TEAMS = "teams"
    }

    suspend fun getAllTeams(): List<TeamInformation>? = try {
        val collection = firebase.db.collection(TEAMS).get().await()
        if (collection.isEmpty) {
            null
        } else {
            collection.documents.map {
                it.toObject(TeamInformation::class.java)!!
            }
        }
    } catch (e: Exception) {
        null
    }

    suspend fun getTeam(team: DocumentReference?): TeamInformation?{
        return try {
            firebase.db.document(team!!.path).get().await()
                .toObject(TeamInformation::class.java)!!
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getTeamInformation(teamReference: String): TeamInformation? {
        return try {
            firebase.db.document(teamReference).get().await()
                .toObject(TeamInformation::class.java)!!
        } catch (e: Exception) {
            null
        }
    }

    fun getReference(home: String): DocumentReference {
        return firebase.db.document(home)
    }
}
