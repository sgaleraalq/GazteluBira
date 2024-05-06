package com.sgalera.gaztelubira.ui

import android.app.Application
import com.sgalera.gaztelubira.domain.InformationList
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation
import com.sgalera.gaztelubira.data.network.services.PlayersApiService
import com.sgalera.gaztelubira.data.network.services.TeamsApiService
import com.sgalera.gaztelubira.domain.model.TeamInformation
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MainApp : Application() {

    @Inject
    lateinit var playersApiService: PlayersApiService

    @Inject
    lateinit var teamsApiService: TeamsApiService

    override fun onCreate() {
        super.onCreate()

        // Save all player data to be accessible to all screens
        CoroutineScope(Dispatchers.IO).launch {
            val playerList = fetchPlayerInformationFromInternet()
            InformationList.players = playerList

            val teamList = fetchTeamInformationFromInternet()
            InformationList.teams = teamList
        }
    }

    private suspend fun fetchPlayerInformationFromInternet(): List<PlayerInformation>? {
        return playersApiService.getAllPlayers()
    }

    private suspend fun fetchTeamInformationFromInternet(): List<TeamInformation>? {
        return teamsApiService.getAllTeams()
    }
}
