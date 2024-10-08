package com.sgalera.gaztelubira.domain.usecases.matches

import android.util.Log
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
        playersStats: List<PlayerStatsModel?>
        ): Boolean {

//        val matchModelInsert = matchesRepository.insertGame(id.toString(), year, matchModel)
//        val matchStatsInsert = matchesRepository.insertMatchStats(id.toString(), year, matchStats)

        val updatedPlayersStats = updatePlayersStats(playersStats, matchStats)

        Log.i("InsertGameUseCase", "Match: $matchModel")
        Log.i("InsertGameUseCase", "MatchStats: $matchStats")
        Log.i("InsertGameUseCase", "MatchModelInsert: $playersStats")
        Log.i("InsertGameUseCase", "PlayersStats: $updatedPlayersStats")
        return false
    }

    private fun updatePlayersStats(playersStats: List<PlayerStatsModel?>, matchStats: MatchStatsModel): List<PlayerModel?> {
        return emptyList()
//        return playersStats.map { playerStats ->
//            if ()
//        }
    }
}
