package com.sgalera.gaztelubira.domain.token

import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
import com.sgalera.gaztelubira.R

object FCMAccessToken {

    fun getAccessToken(context: Context): String {
        val inputStream = context.resources.openRawResource(R.raw.gaztelu_bira_credentials)
        val googleCredentials = GoogleCredentials
            .fromStream(inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))

        googleCredentials.refreshIfExpired()
        return googleCredentials.accessToken.tokenValue
    }
}
