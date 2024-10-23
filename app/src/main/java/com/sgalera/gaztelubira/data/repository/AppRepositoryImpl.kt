package com.sgalera.gaztelubira.data.repository

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.sgalera.gaztelubira.domain.model.app.PushNotification
import com.sgalera.gaztelubira.domain.repository.AppRepository
import com.sgalera.gaztelubira.domain.repository.NotificationAPI
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
    private val notificationAPI: NotificationAPI,
    @ApplicationContext private val context: Context
): AppRepository {

    companion object {
        const val MIN_VERSION_RC = "min_version"
    }

    override fun getAppVersion(): List<Int> {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName.split(".").map { it.toInt() }
        } catch (e: Exception){
            listOf(0, 0, 0)
        }
    }

    override suspend fun getMinAllowedVersion(): List<Int> {
        remoteConfig.fetch(0).await()
        remoteConfig.activate().await()
        val minVersion = remoteConfig.getString(MIN_VERSION_RC)
        if (minVersion.isBlank()) return listOf(0, 0, 0)
        return minVersion.split(".").map { it.toInt() }
    }

    override suspend fun sendNotification(notification: PushNotification): Response<ResponseBody> {
        return notificationAPI.sendNotification(notification)
    }
}