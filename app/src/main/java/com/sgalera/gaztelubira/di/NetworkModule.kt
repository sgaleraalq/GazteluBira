package com.sgalera.gaztelubira.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sgalera.gaztelubira.data.repository.MatchesRepositoryImpl
import com.sgalera.gaztelubira.data.repository.PlayersRepositoryImpl
import com.sgalera.gaztelubira.domain.model.PlayerModel
import com.sgalera.gaztelubira.domain.repository.MatchesRepository
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import com.sgalera.gaztelubira.ui.manager.PasswordManager
import com.sgalera.gaztelubira.ui.manager.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Year
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
    fun providePlayersRepository(firestore: FirebaseFirestore): PlayersRepository = PlayersRepositoryImpl(firestore)

    @Provides
    fun provideMatchesRepository(firestore: FirebaseFirestore): MatchesRepository = MatchesRepositoryImpl(firestore)
}