package com.sgalera.gaztelubira.data.response

import com.google.gson.annotations.SerializedName
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo

data class MatchInfoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("match") val match: String,
    @SerializedName("home_team") val homeTeam: String,
    @SerializedName("away_team") val awayTeam: String,
    @SerializedName("home_goals") val homeGoals: Int,
    @SerializedName("away_goals") val awayGoals: Int
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