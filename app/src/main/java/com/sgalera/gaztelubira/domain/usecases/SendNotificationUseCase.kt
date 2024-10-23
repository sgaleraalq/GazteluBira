package com.sgalera.gaztelubira.domain.usecases

import android.util.Log
import com.sgalera.gaztelubira.domain.model.app.NotificationData
import com.sgalera.gaztelubira.domain.model.app.PushNotification
import com.sgalera.gaztelubira.domain.repository.AppRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class SendNotificationUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(token: String, title: String, message: String): Response<ResponseBody> {
        val notification = PushNotification(
            NotificationData(title, message),
            token
        )
        Log.i("SendNotificationUseCase", "invoke: $notification")
        return appRepository.sendNotification(notification)
    }
}
