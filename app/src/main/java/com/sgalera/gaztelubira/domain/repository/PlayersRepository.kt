package com.sgalera.gaztelubira.domain.repository

interface PlayersRepository {

    suspend fun getPlayers()

    suspend fun getPlayersStats(year: Int): Any

}