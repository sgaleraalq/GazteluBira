package com.sgalera.gaztelubira.domain.usecases.players

import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import javax.inject.Inject

class GetPlayersStatsUseCase @Inject constructor(
    private val playersRepository: PlayersRepository
) {
    suspend operator fun invoke(year: String) = playersRepository.getPlayersStats(year)
}
