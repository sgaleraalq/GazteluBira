package com.sgalera.gaztelubira.domain.model.matches

import com.sgalera.gaztelubira.domain.model.teams.TeamModel
import com.sgalera.gaztelubira.domain.model.players.PlayerModel

data class MatchStatsModel(
    val id: Int = 0,
    var match: String? = null,
    val journey: Int = -1,
    var homeTeam: TeamModel? = null,
    var awayTeam: TeamModel? = null,
    var homeGoals: Int = -1,
    var awayGoals: Int = -1,
    val scorers: List<PlayerModel?> = emptyList(),
    val assistants: List<PlayerModel?> = emptyList(),
    val starters: MutableMap<String, PlayerModel?> = mutableMapOf(
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
    var bench: List<PlayerModel?> = emptyList()
)