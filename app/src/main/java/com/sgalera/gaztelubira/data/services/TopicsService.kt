package com.sgalera.gaztelubira.data.services

import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class TopicsService @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging
) {

    companion object {
        const val TOPIC_ALL = "all"
    }

    fun subscribeToTopic(topic: String) {
        firebaseMessaging.subscribeToTopic(topic).addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                 Log.i("TopicsService", "Subscribed to topic $topic")
            } else {
                // Log.e("TopicsService", "Error subscribing to topic $topic")
            }
        }
    }

    fun unsubscribeFromTopic(topic: String) {
        firebaseMessaging.unsubscribeFromTopic(topic)
    }
}