package com.sgalera.gaztelubira.domain.repository

interface AppRepository {
    fun getAppVersion(): List<Int>
    suspend fun getMinAllowedVersion(): List<Int>
}