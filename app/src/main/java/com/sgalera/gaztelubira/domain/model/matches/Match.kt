package com.sgalera.gaztelubira.domain.model.matches

import com.sgalera.gaztelubira.domain.model.Team
import com.sgalera.gaztelubira.domain.model.TeamInformation
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

data class Match(
    val id: Int,
    val match: String,
    val local: TeamInformation,
    val visitor: TeamInformation,
    val localGoals: Int,
    val visitorGoals: Int,
    val scorers: List<String>,
    val assistants: List<String>,
    val starters: Map<String, PlayerInformation?>,
    val bench: List<PlayerInformation?>
)

