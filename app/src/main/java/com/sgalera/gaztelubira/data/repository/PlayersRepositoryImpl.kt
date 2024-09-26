package com.sgalera.gaztelubira.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import javax.inject.Inject

class PlayersRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): PlayersRepository {
    override suspend fun getPlayers() {

    }

    override suspend fun getPlayersStats(year: Int): Any {
        return 1
    }
}