package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo

data class MatchInfoResponse(
    val id: Int = 0,
    val match: String = "",
    @get:PropertyName("home_team") @set:PropertyName("home_team") var homeTeam: String = "",
    @get:PropertyName("away_team") @set:PropertyName("away_team") var awayTeam: String = "",
    @get:PropertyName("home_goals") @set:PropertyName("home_goals") var homeGoals: Int = 0,
    @get:PropertyName("away_goals") @set:PropertyName("away_goals") var awayGoals: Int = 0
) {
    fun toDomain(): MatchInfo {
        return MatchInfo(
            id = id,
            match = match,
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeGoals = homeGoals,
            awayGoals = awayGoals
        )
    }
}