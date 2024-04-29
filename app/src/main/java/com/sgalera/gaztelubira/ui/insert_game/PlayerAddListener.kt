package com.sgalera.gaztelubira.ui.insert_game

import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import java.text.DecimalFormat

interface PlayerAddListener {
    fun onPlayerAdded(player: PlayerInfo)
    fun getPercentage(player: PlayerStats): String {
        val total = player.goals + player.assists + player.cleanSheet + player.penalties
        val gamesPlayed = player.gamesPlayed.toFloat()
        return if (gamesPlayed != 0f) {
            val percentage = (total.toFloat() / gamesPlayed)
            DecimalFormat("#.##").format(percentage)
        } else {
            "0"
        }
    }
}