package com.sgalera.gaztelubira.domain.usecases.players

import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import javax.inject.Inject

class GetPlayerStatsUseCase @Inject constructor(
    private val playersRepository: PlayersRepository
) {
    suspend operator fun invoke(playerName: String, year: String) =
        playersRepository.getPlayerStats(playerName, year)
}
