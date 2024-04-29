package com.sgalera.gaztelubira.domain.model.players

data class PlayerStats(
    val information: PlayerInformation?,
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
            "name" to information!!.name,
            "goals" to goals,
            "assists" to assists,
            "games_played" to gamesPlayed,
            "penalties" to penalties,
            "clean_sheet" to cleanSheet,
            "position" to position,
            "last_ranking" to lastRanking,
            "ranking" to ranking
        )
    }
}