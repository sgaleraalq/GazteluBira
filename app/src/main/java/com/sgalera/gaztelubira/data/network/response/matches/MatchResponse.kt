package com.sgalera.gaztelubira.data.network.response.matches

import com.google.gson.annotations.SerializedName
import com.sgalera.gaztelubira.domain.model.matches.Match
import com.sgalera.gaztelubira.domain.model.matches.Starters

//data class MatchResponse (
//    @SerializedName("home_team") val homeTeam: TeamResponse,
//    @SerializedName("away_team") val awayTeam: TeamResponse,
//    @SerializedName("home_goals") val homeGoals: Int,
//    @SerializedName("away_goals") val awayGoals: Int,
//    @SerializedName("scorers") val scorers: List<String>,
//    @SerializedName("assitants") val assitants: List<String>,
//    @SerializedName("starters") val starters: Starters,
//    @SerializedName("bench") val bench: List<String>
//){
//    fun toDomain() = Match(
//        local = homeTeam,
//        visitor = awayTeam,
//        localGoals = homeGoals,
//        visitorGoals = awayGoals,
//        scorers = scorers,
//        assitants = assitants,
//        starters = starters,
//        bench = bench
//    )
//}
//
//class TeamResponse {
//
//}
