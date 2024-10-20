package com.sgalera.gaztelubira.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.data.repository.AppRepositoryImpl
import com.sgalera.gaztelubira.data.repository.MatchesRepositoryImpl
import com.sgalera.gaztelubira.data.repository.PlayersRepositoryImpl
import com.sgalera.gaztelubira.data.repository.TeamsRepositoryImpl
import com.sgalera.gaztelubira.domain.repository.MatchesRepository
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import com.sgalera.gaztelubira.domain.repository.TeamsRepository
import com.sgalera.gaztelubira.domain.manager.PasswordManager
import com.sgalera.gaztelubira.domain.manager.SharedPreferences
import com.sgalera.gaztelubira.domain.repository.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideFirestore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context) = SharedPreferences(context)

    @Provides
    @Singleton
    fun providePasswordManager() = PasswordManager()

    @Provides
    @Singleton
    fun provideRemoteConfig() = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            }
        )
        setDefaultsAsync(R.xml.remote_config_defaults)
        fetchAndActivate()
    }

    @Provides
    @Singleton
    fun providePlayersRepository(firestore: FirebaseFirestore): PlayersRepository =
        PlayersRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideTeamsRepository(firestore: FirebaseFirestore): TeamsRepository =
        TeamsRepositoryImpl(firestore)

    @Provides
    fun provideMatchesRepository(firestore: FirebaseFirestore): MatchesRepository =
        MatchesRepositoryImpl(firestore)

    @Provides
    fun provideAppRepository(
        @ApplicationContext context: Context,
        remoteConfig: FirebaseRemoteConfig
    ): AppRepository = AppRepositoryImpl(remoteConfig, context)

}