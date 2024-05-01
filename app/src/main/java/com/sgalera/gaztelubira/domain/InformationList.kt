package com.sgalera.gaztelubira.domain

import com.sgalera.gaztelubira.domain.model.TeamInformation
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

object InformationList {
    var players: List<PlayerInformation>? = emptyList()
    var teams: List<TeamInformation>? = emptyList()
}