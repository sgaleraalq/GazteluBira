package com.sgalera.gaztelubira.domain.repository

import com.sgalera.gaztelubira.domain.model.app.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response

interface AppRepository {
    fun getAppVersion(): List<Int>
    suspend fun getMinAllowedVersion(): List<Int>
    suspend fun sendNotification(notification: PushNotification): Response<ResponseBody>
}