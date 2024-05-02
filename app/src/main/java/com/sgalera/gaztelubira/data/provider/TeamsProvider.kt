package com.sgalera.gaztelubira.data.provider

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.data.network.services.TeamsApiService
import javax.inject.Inject

class TeamsProvider @Inject constructor(private val teamsApiService: TeamsApiService){
    suspend fun getTeamInformation(teamReference: String) = teamsApiService.getTeamInformation(teamReference)
}
