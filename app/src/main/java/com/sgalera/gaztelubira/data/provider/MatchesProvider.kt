package com.sgalera.gaztelubira.data.provider

import com.google.firebase.Firebase
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo
import javax.inject.Inject

class MatchesProvider @Inject constructor() {
    fun getMatches(): List<MatchInfo> {
        return listOf(
            MatchInfo(
                id = 1,
                homeTeam = "Gaztelu Bira",
                awayTeam = "Esmeraldeños",
                homeGoals = 2,
                awayGoals = 3
            ),
            MatchInfo(
                id = 2,
                homeTeam = "Aterbea",
                awayTeam = "Gaztelu Bira",
                homeGoals = 2,
                awayGoals = 2
            ),
            MatchInfo(
                id = 3,
                homeTeam = "Gaztelu Bira",
                awayTeam = "Anaitasuna",
                homeGoals = 3,
                awayGoals = 2
            )
        )
    }
}