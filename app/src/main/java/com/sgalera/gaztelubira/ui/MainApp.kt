package com.sgalera.gaztelubira.ui

import android.app.Application
import com.sgalera.gaztelubira.domain.model.TeamInformation
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class MainApp : Application() {

//    override fun onCreate() {
//        super.onCreate()

        // Save all player data to be accessible to all screens
//        CoroutineScope(Dispatchers.IO).launch {
//            val playerList = fetchPlayerInformationFromInternet()
//            InformationList.players = playerList
//
//            val teamList = fetchTeamInformationFromInternet()
//            InformationList.teams = teamList
//        }
//    }
//
//    private suspend fun fetchPlayerInformationFromInternet(): List<PlayerInformation>? {
//        return playersApiService.getAllPlayers()
//    }
//
//    private suspend fun fetchTeamInformationFromInternet(): List<TeamInformation>? {
//        return teamsApiService.getAllTeams()
//    }
}
