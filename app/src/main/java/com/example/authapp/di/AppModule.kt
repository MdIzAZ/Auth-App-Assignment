package com.example.authapp.di

import android.app.Application
import androidx.room.Room
import com.example.authapp.data.CustomerDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application,
    ): CustomerDb {
        return Room
            .databaseBuilder(
                application,
                CustomerDb::class.java,
                "Customers"
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}