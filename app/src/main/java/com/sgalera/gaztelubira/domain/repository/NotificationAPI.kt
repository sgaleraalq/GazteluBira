package com.sgalera.gaztelubira.domain.repository

import com.sgalera.gaztelubira.domain.model.app.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key=YOUR_SERVER_KEY", "Content-Type: application/json")
    @POST("fcm/send")
    suspend fun sendNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}