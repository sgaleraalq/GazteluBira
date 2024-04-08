package com.sgalera.gaztelubira.ui.insert_game

import com.sgalera.gaztelubira.domain.model.players.PlayerInfo

interface PlayerAddListener {
    fun onPlayerAdded(player: PlayerInfo)
}