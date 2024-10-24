package com.sgalera.gaztelubira.domain.model.app

data class PushNotification(
    val message: Message
)

data class Message(
    val topic: String,
    val notification: Notification? = null,
    val data: Map<String, String>? = null
)

data class Notification(
    val title: String,
    val body: String
)