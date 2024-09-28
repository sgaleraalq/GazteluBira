package com.sgalera.gaztelubira.domain.usecases.matches

import com.sgalera.gaztelubira.domain.repository.MatchesRepository
import javax.inject.Inject

class GetMatchStatsUseCase @Inject constructor(
    private val matchesRepository: MatchesRepository
) {
    suspend operator fun invoke(id: String, year: String) = matchesRepository.getMatchStats(id, year)
}
