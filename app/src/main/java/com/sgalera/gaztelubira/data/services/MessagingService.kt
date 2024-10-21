package com.sgalera.gaztelubira.data.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import javax.inject.Inject

class MessagingService @Inject constructor(): FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendRegistrationToken(token)

    }

    private fun sendRegistrationToken(token: String) {
        Log.i("MessagingService", "Token: $token")
    }
}