package com.sgalera.gaztelubira.domain.usecases.players

import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import javax.inject.Inject

class GetPlayersUseCase @Inject constructor(
    private val playersRepository: PlayersRepository
) {
    suspend operator fun invoke(year: String) = playersRepository.getPlayers(year)
}
