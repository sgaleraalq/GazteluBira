package com.sgalera.gaztelubira.data.network.services

import android.util.Log
import com.sgalera.gaztelubira.data.response.MatchInfoResponse
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo
import kotlinx.coroutines.tasks.await


interface MatchesApiService {
    companion object {
        const val MATCHES_INFO_COLLECTION = "matches"
        const val MATCHES_STATS_COLLECTION = "matches_stats"
    }
    suspend fun getMatchesInfo(): MatchInfoResponse
}