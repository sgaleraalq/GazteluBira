package com.sgalera.gaztelubira.domain.usecases

import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayerModelUseCase
import javax.inject.Inject

class GetPlayersStatsUseCase @Inject constructor(
    private val playersRepository: PlayersRepository
) {
    suspend operator fun invoke(year: String, getPlayerModelUseCase: GetPlayerModelUseCase) =
        playersRepository.getPlayersStats(year, getPlayerModelUseCase)
}
