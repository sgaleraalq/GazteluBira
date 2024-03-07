package com.sgalera.gaztelubira.data.provider

import com.sgalera.gaztelubira.domain.model.Team.*
import com.sgalera.gaztelubira.domain.model.matches.Match
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo
import com.sgalera.gaztelubira.domain.model.matches.Starters
import javax.inject.Inject

class MatchesProvider @Inject constructor() {
    fun getMatches(): List<MatchInfo> {
        return listOf(
            MatchInfo(
                id = 1,
                homeTeam = "Gaztelu Bira",
                awayTeam = "Esmeraldeños",
                homeGoals = 3,
                awayGoals = 2
            ),
            MatchInfo(
                id = 2,
                homeTeam = "Aterbea",
                awayTeam = "Gaztelu Bira",
                homeGoals = 3,
                awayGoals = 2
            )
        )
    }
}