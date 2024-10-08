package com.sgalera.gaztelubira.domain.usecases.matches

import android.util.Log
import com.sgalera.gaztelubira.domain.model.matches.MatchModel
import com.sgalera.gaztelubira.domain.model.matches.MatchStatsModel
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.domain.repository.MatchesRepository
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import java.text.DecimalFormat
import javax.inject.Inject

class InsertGameUseCase @Inject constructor(
    private val matchesRepository: MatchesRepository,
    private val playersRepository: PlayersRepository
) {

    suspend operator fun invoke(
        year: String,
        id: Int,
        journey: Int,
        matchModel: MatchModel,
        matchStats: MatchStatsModel,
        playersStats: List<PlayerStatsModel?>,
        cleanSheet: List<PlayerModel?>
    ): Boolean {

//        val matchModelInsert = matchesRepository.insertGame(id.toString(), year, matchModel.copy(id = id, journey = journey))
//        val matchStatsInsert = matchesRepository.insertMatchStats(id.toString(), year, matchStats.copy(id = id, journey = journey))

        val updatedPlayersStats = updatePlayersStats(playersStats, matchStats, cleanSheet)

        return false
    }

    private fun updatePlayersStats(
        playersStats: List<PlayerStatsModel?>,
        matchStats: MatchStatsModel,
        cleanSheet: List<PlayerModel?>
    ): List<PlayerModel?> {
        val starters = matchStats.starters.values.map { it?.ownReference }
        val bench = matchStats.bench.map { it?.ownReference }
        val scorers = matchStats.scorers.map { it?.ownReference }
        val assistants = matchStats.assistants.map { it?.ownReference }
        val penalties = matchStats.penalties.map { it?.ownReference }
        val clean = cleanSheet.map { it?.ownReference }

        Log.i("InsertGameUseCase", "scorers: $scorers")
        Log.i("InsertGameUseCase", "assistants: $assistants")
        Log.i("InsertGameUseCase", "penalties: $penalties")
        Log.i("InsertGameUseCase", "clean: $clean")

        playersStats.forEach { player ->
            if (player != null) {
                when (player.information?.ownReference) {
                    in starters, in bench -> {
                        player.gamesPlayed = player.gamesPlayed.plus(1)
                    }
                    in scorers -> {
                        Log.i("InsertGameUseCase", "Goooal: ${player.information?.name}")
                        player.goals = player.goals.plus(1)
                    }
                    in assistants -> {
                        Log.i("InsertGameUseCase", "Assist: ${player.information?.name}")
                        player.assists = player.assists.plus(1)
                    }
                    in penalties -> {
                        Log.i("InsertGameUseCase", "Penalty: ${player.information?.name}")
                        player.penalties = player.penalties.plus(1)
                    }
                    in clean -> {
                        Log.i("InsertGameUseCase", "Clean: ${player.information?.name}")
                        player.cleanSheet = player.cleanSheet.plus(1)
                    }
                }

                player.lastRanking = player.ranking
                player.percentage = getPercentage(player)
            }
        }

        playersStats.sortedByDescending { it?.percentage }
        playersStats.forEachIndexed { index, player ->
            if (player != null) {
                player.ranking = index.plus(1)
            }
        }

        playersStats.forEach {
            Log.i(
                "InsertGameUseCase",
                "player: ${it?.information?.name} ${it?.goals} ${it?.assists} ${it?.cleanSheet} ${it?.penalties} ${it?.gamesPlayed} ${it?.ranking} ${it?.lastRanking} ${it?.percentage}"
            )
        }

        return emptyList()
//        return playersStats.map { player ->
//            if (player?.reference in starters || player?.reference in bench) {
//                player?.gamesPlayed = player?.gamesPlayed?.plus(1)
//            }
//        }
    }

    private fun getPercentage(player: PlayerStatsModel): String {
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
