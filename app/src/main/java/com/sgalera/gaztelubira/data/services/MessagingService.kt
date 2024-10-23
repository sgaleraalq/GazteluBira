package com.sgalera.gaztelubira.data.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject

class MessagingService @Inject constructor(): FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data.isNotEmpty()){
            val match = message.data["match"].orEmpty()
            Log.i("MessagingService", "Match: $match")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendRegistrationToken(token)
    }


    private fun sendRegistrationToken(token: String) {
        Log.i("MessagingService", "Token: $token")
    }
}