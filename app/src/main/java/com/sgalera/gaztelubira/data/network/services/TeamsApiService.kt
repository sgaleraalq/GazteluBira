package com.sgalera.gaztelubira.data.network.services

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
}
