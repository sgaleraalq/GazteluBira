package com.sgalera.gaztelubira.domain.model.matches

import com.sgalera.gaztelubira.domain.model.teams.TeamModel
import com.sgalera.gaztelubira.domain.model.players.PlayerModel

data class MatchStatsModel(
    val id: Int = 0,
    val match: String? = null,
    val journey: Int = -1,
    var homeTeam: TeamModel? = null,
    var awayTeam: TeamModel? = null,
    val homeGoals: Int = -1,
    val awayGoals: Int = -1,
    val scorers: List<PlayerModel?> = emptyList(),
    val assistants: List<PlayerModel?> = emptyList(),
    val starters: Map<String, PlayerModel?> = mapOf(
        "goal_keeper" to null,
        "left_back" to null,
        "right_back" to null,
        "left_centre_back" to null,
        "right_centre_back" to null,
        "defensive_mid_fielder" to null,
        "left_mid_fielder" to null,
        "right_mid_fielder" to null,
        "left_striker" to null,
        "right_striker" to null,
        "striker" to null
    ),
    val bench: List<PlayerModel?> = emptyList()
)