package com.sgalera.gaztelubira.di

import android.content.Context
import com.sgalera.gaztelubira.ui.manager.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context) = SharedPreferences(context)
}