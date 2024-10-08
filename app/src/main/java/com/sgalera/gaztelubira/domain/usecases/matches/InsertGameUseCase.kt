package com.sgalera.gaztelubira.domain.usecases.matches

import com.sgalera.gaztelubira.domain.model.matches.MatchModel
import com.sgalera.gaztelubira.domain.model.matches.MatchStatsModel
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.domain.repository.MatchesRepository
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
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
        val matchModelInsert = matchesRepository.insertGame(id.toString(), year, matchModel.copy(id = id, journey = journey))
        val matchStatsInsert = matchesRepository.insertMatchStats(id.toString(), year, matchStats.copy(id = id, journey = journey))
        val updatedPlayersStats = updatePlayersStats(playersStats, matchStats, cleanSheet)
        val playersStatsInsert = playersRepository.insertPlayersStats(year, updatedPlayersStats)

        return matchModelInsert && matchStatsInsert && playersStatsInsert
    }

    private fun updatePlayersStats(
        playersStats: List<PlayerStatsModel?>,
        matchStats: MatchStatsModel,
        cleanSheet: List<PlayerModel?>
    ): List<PlayerStatsModel?> {
        val starters = matchStats.starters.values.map { it?.ownReference }
        val bench = matchStats.bench.map { it?.ownReference }
        val scorers = matchStats.scorers.map { it?.ownReference }
        val assistants = matchStats.assistants.map { it?.ownReference }
        val penalties = matchStats.penalties.map { it?.ownReference }
        val clean = cleanSheet.map { it?.ownReference }

        val updatedList = mutableListOf<PlayerStatsModel?>()

        playersStats.forEach { player ->
            if (player != null) {
                if (player.information?.ownReference in starters || player.information?.ownReference in bench) {
                    player.gamesPlayed = player.gamesPlayed.plus(1)
                }
                if (player.information?.ownReference in scorers) {
                    player.goals = player.goals.plus(1)
                }
                if (player.information?.ownReference in assistants) {
                    player.assists = player.assists.plus(1)
                }
                if (player.information?.ownReference in penalties) {
                    player.penalties = player.penalties.plus(1)
                }
                if (player.information?.ownReference in clean) {
                    player.cleanSheet = player.cleanSheet.plus(1)
                }

                player.lastRanking = player.ranking
                player.sortedPercentage = getPercentage(player)
            }
            updatedList.add(player)
        }

        val sortedUpdatedList = updatedList
            .sortedByDescending { it?.sortedPercentage }
            .mapIndexed { index, player ->
                player?.apply { ranking = index + 1 }
            }

        return sortedUpdatedList
    }

    private fun getPercentage(player: PlayerStatsModel): Float {
        val total = player.goals + player.assists + player.cleanSheet + player.penalties
        val gamesPlayed = player.gamesPlayed.toFloat()
        return if (gamesPlayed != 0f) {
            total.toFloat() / gamesPlayed
        } else {
            0f
        }
    }
}
