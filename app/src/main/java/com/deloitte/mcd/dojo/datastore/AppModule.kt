package com.deloitte.mcd.dojo.datastore

import android.content.Context
import android.content.SharedPreferences
import com.deloitte.mcd.dojo.datastore.model.SharedPreferenceRepository
import com.deloitte.mcd.dojo.datastore.model.TasksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val USER_PREFERENCES_NAME = "user_preferences"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPreferenceRepository(sharedPreferences: SharedPreferences): SharedPreferenceRepository =
        SharedPreferenceRepository(sharedPreferences)

    @Provides
    @Singleton
    fun provideRepository(): TasksRepository = TasksRepository()
}