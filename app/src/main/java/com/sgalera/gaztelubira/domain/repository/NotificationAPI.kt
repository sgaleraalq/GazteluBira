package com.sgalera.gaztelubira.domain.repository

import com.sgalera.gaztelubira.core.Constants.CONTENT_TYPE
import com.sgalera.gaztelubira.core.Constants.PROJECT_ID
import com.sgalera.gaztelubira.domain.model.app.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Content-Type:${CONTENT_TYPE}", "Accept:${CONTENT_TYPE}")
    @POST("v1/projects/${PROJECT_ID}/messages:send")
    suspend fun sendNotification(
        @Body notification: PushNotification,
        @Header("Authorization") accessToken: String
    ): Response<ResponseBody>
}