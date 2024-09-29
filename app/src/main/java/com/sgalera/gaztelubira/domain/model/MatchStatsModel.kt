package com.sgalera.gaztelubira.domain.model

data class MatchStatsModel(
    val match: String,
    val homeTeam: TeamModel?,
    val awayTeam: TeamModel?,
    val homeGoals: Int,
    val awayGoals: Int,
    val scorers: List<PlayerModel?>,
    val assistants: List<PlayerModel?>,
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