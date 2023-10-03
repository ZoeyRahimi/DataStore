package com.deloitte.mcd.dojo.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import com.deloitte.mcd.dojo.datastore.model.UserPreferencesSerializer
import com.deloitte.mcd.dojo.datastore.model.data.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val DATA_STORE_FILE_NAME = "user_prefs.pb"
private const val SORT_ORDER_KEY = "sort_order"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideProtoDataStore(@ApplicationContext context: Context): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler { UserPreferences.getDefaultInstance() },
            produceFile = { File(context.filesDir, DATA_STORE_FILE_NAME) },
            migrations = listOf(SharedPreferencesMigration(
                context,
                USER_PREFERENCES_NAME
            ) { sharedPrefs: SharedPreferencesView, currentData: UserPreferences ->
                // Define the mapping from SharedPreferences to UserPreferences
                if (currentData.sortOrder == UserPreferences.SortOrder.UNSPECIFIED) {
                    currentData.toBuilder().setSortOrder(
                        UserPreferences.SortOrder.valueOf(
                            sharedPrefs.getString(
                                SORT_ORDER_KEY,
                                UserPreferences.SortOrder.NONE.name
                            )!!
                        )
                    ).build()
                } else {
                    currentData
                }
            })
        )
}