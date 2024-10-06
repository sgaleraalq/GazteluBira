package com.sgalera.gaztelubira.domain.model.matches

import com.sgalera.gaztelubira.domain.model.teams.TeamModel
import com.sgalera.gaztelubira.domain.model.players.PlayerModel

data class MatchStatsModel(
    val id: Int? = null,
    val match: String? = null,
    val journey: Int? = null,
    val homeTeam: TeamModel? = null,
    val awayTeam: TeamModel? = null,
    val homeGoals: Int? = null,
    val awayGoals: Int? = null,
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