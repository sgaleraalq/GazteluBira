package com.sgalera.gaztelubira.domain.model.players

data class PlayerStats(
    val name: PlayerInfo,
    var goals: Int,
    var assists: Int,
    var gamesPlayed: Int,
    var penalties: Int,
    var cleanSheet: Int,
    val position: String,
    var lastRanking: Int,
    var ranking: Int,
    var percentage: String? = null
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "name" to name.name,
            "goals" to goals,
            "assists" to assists,
            "gamesPlayed" to gamesPlayed,
            "penalties" to penalties,
            "cleanSheet" to cleanSheet,
            "position" to position,
            "lastRanking" to lastRanking,
            "ranking" to ranking
        )
    }
}