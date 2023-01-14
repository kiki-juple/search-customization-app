package com.kiki.searchcustomization.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesScopeModule {

    @Singleton
    @Provides
    fun provideCoroutineScope(@IoDispatcher ioDispatcher: CoroutineDispatcher): CoroutineScope =
        CoroutineScope(SupervisorJob() + ioDispatcher)
}