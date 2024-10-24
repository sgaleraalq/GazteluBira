package com.sgalera.gaztelubira.domain.usecases.app

import android.util.Log
import com.sgalera.gaztelubira.domain.model.app.Message
import com.sgalera.gaztelubira.domain.model.app.Notification
import com.sgalera.gaztelubira.domain.model.app.PushNotification
import com.sgalera.gaztelubira.domain.repository.AppRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class SendNotificationUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(topics: String, title: String, message: String): Response<ResponseBody> {
        val notification = PushNotification(
            message = Message(
                topic = topics,
                notification = Notification(
                    title = title,
                    body = message
                )
            )
        )
        Log.i("SendNotificationUseCase", "invoke: $notification")
        return appRepository.sendNotification(notification)
    }
}
