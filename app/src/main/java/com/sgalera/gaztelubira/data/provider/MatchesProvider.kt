package com.sgalera.gaztelubira.data.provider

import com.sgalera.gaztelubira.data.network.services.MatchesApiService
import com.sgalera.gaztelubira.data.network.services.TeamsApiService
import com.sgalera.gaztelubira.data.response.MatchInfoResponse
import com.sgalera.gaztelubira.data.response.MatchResponse
import com.sgalera.gaztelubira.domain.model.TeamInformation
import com.sgalera.gaztelubira.domain.model.matches.Match
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo
import javax.inject.Inject

class MatchesProvider @Inject constructor(private val matchesApiService: MatchesApiService, private val teamsApiService: TeamsApiService) {

    suspend fun getTeams(): List<TeamInformation>? {
        return teamsApiService.getAllTeams()
    }

    suspend fun getMatches(): List<MatchInfo>? {
        return matchesApiService.getMatchesInfo()
    }

    suspend fun getMatch(id: Int): Match? {
        return matchesApiService.getMatch(id)
    }

    suspend fun postGame(match: MatchInfoResponse): Boolean {
        return matchesApiService.postGame(match)
    }

    suspend fun postGameStats(matchStats: MatchResponse): Boolean {
        return matchesApiService.postGameStats(matchStats)
    }
}