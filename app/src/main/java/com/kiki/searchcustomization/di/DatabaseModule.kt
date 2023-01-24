package com.kiki.searchcustomization.di

import android.content.Context
import androidx.room.Room
import com.kiki.searchcustomization.data.dao.WartegDao
import com.kiki.searchcustomization.data.database.WartegDatabase
import com.kiki.searchcustomization.util.algorithm.MultiFactorEvaluator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        callback: WartegDatabase.PrepopulateCallback,
    ): WartegDatabase {
        return Room.databaseBuilder(
            context,
            WartegDatabase::class.java,
            "wartegs.db"
        )
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideDao(database: WartegDatabase): WartegDao = database.dao()

    @Provides
    fun provideMultiFactorEvaluator(): MultiFactorEvaluator = MultiFactorEvaluator()
}