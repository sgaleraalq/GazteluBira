package com.sgalera.gaztelubira.data.provider

import com.sgalera.gaztelubira.data.network.services.MatchesApiService
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo
import javax.inject.Inject

class MatchesProvider @Inject constructor(private val matchesApiService: MatchesApiService) {
    suspend fun getMatches(): List<MatchInfo>? {
        matchesApiService.getMatchesInfo()
        return listOf(
            MatchInfo(
                id = 1,
                match = "liga",
                homeTeam = "Gaztelu Bira",
                awayTeam = "Esmeraldeños",
                homeGoals = 2,
                awayGoals = 3
            ),
            MatchInfo(
                id = 2,
                match = "copa",
                homeTeam = "Aterbea",
                awayTeam = "Gaztelu Bira",
                homeGoals = 2,
                awayGoals = 2
            ),
            MatchInfo(
                id = 3,
                match = "liga",
                homeTeam = "Gaztelu Bira",
                awayTeam = "Anaitasuna",
                homeGoals = 3,
                awayGoals = 2
            )
        )
    }
}