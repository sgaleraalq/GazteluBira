package com.sgalera.gaztelubira.domain.usecases.players

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import javax.inject.Inject

class GetPlayerModelUseCase @Inject constructor(
    private val playersRepository: PlayersRepository
) {
    suspend operator fun invoke(reference: DocumentReference?) = playersRepository.getPlayerModel(reference)
}
