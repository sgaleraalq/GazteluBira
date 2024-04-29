package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.data.network.firebase.FirebaseClient
import com.sgalera.gaztelubira.data.network.services.PlayersApiService
import com.sgalera.gaztelubira.domain.model.MappingUtils.mapTeam
import com.sgalera.gaztelubira.domain.model.matches.Match
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation
import kotlinx.coroutines.runBlocking

data class MatchResponse(
    val id: Int = 0,
    val match: String = "",
    val scorers: List<String> = emptyList(),
    val assistants: List<String> = emptyList(),
    val starters: Map<String, String> = emptyMap(),
    val bench: List<String> = emptyList(),
    @get:PropertyName("home_team") @set:PropertyName("home_team") var homeTeam: String = "",
    @get:PropertyName("away_team") @set:PropertyName("away_team") var awayTeam: String = "",
    @get:PropertyName("home_goals") @set:PropertyName("home_goals") var homeGoals: Int = 0,
    @get:PropertyName("away_goals") @set: PropertyName("away_goals") var awayGoals: Int = 0
) {
    fun toDomain() = Match(
        id = id,
        match = match,
        local = mapTeam(homeTeam),
        visitor = mapTeam(awayTeam),
        localGoals = homeGoals,
        visitorGoals = awayGoals,
        scorers = scorers,
        assistants = assistants,
        starters = starters.mapValues { runBlocking { mapPlayerInformation(it.value) } },
        bench = bench.map { runBlocking { mapPlayerInformation(it) } }
    )

    private suspend fun mapPlayerInformation(player: String): PlayerInformation? {
        return try{
            val apiService = PlayersApiService(firebase = FirebaseClient())
            apiService.playerInformation(player)
        } catch (e: Exception) {
            PlayerInformation(
                name = "Error",
                surname = "Error",
                img = "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/img_no_profile_picture.webp?alt=media&token=3704b325-2ffc-42eb-892d-147c3860d8b1",
                dorsal = 0,
                stats = null,
                selected = false
            )
        }
    }
}

