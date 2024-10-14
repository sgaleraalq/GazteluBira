package com.sgalera.gaztelubira.domain.usecases.season

import com.sgalera.gaztelubira.domain.repository.TeamsRepository
import javax.inject.Inject

class GetSeasonsUseCase @Inject constructor(
    private val teamsRepository: TeamsRepository
) {
    suspend operator fun invoke() = teamsRepository.getSeasons()
}
