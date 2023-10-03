package com.deloitte.mcd.dojo.datastore.model

import androidx.datastore.core.DataStore
import com.deloitte.mcd.dojo.datastore.model.data.Task
import com.deloitte.mcd.dojo.datastore.model.data.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(private val dataStore: DataStore<UserPreferences>) {

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun updateShowCompleted(show: Boolean) {
        dataStore.updateData { preferences ->
            preferences
                .toBuilder()
                .setShowCompleted(show)
                .build()
        }
    }

    suspend fun enableSortByDeadline(enable: Boolean) {
        dataStore.updateData { preferences ->
            val currentOrder = preferences.sortOrder
            val newSortOrder =
                if (enable) {
                    if (currentOrder == UserPreferences.SortOrder.BY_PRIORITY) {
                        UserPreferences.SortOrder.BY_DEADLINE_AND_PRIORITY
                    } else {
                        UserPreferences.SortOrder.BY_DEADLINE
                    }
                } else {
                    if (currentOrder == UserPreferences.SortOrder.BY_DEADLINE_AND_PRIORITY) {
                        UserPreferences.SortOrder.BY_PRIORITY
                    } else {
                        UserPreferences.SortOrder.NONE
                    }
                }
            preferences.toBuilder()
                .setSortOrder(newSortOrder)
                .build()
        }
    }

    suspend fun enableSortByPriority(enable: Boolean) {
        dataStore.updateData { preferences ->
            val currentOrder = preferences.sortOrder
            val newSortOrder =
                if (enable) {
                    if (currentOrder == UserPreferences.SortOrder.BY_DEADLINE) {
                        UserPreferences.SortOrder.BY_DEADLINE_AND_PRIORITY
                    } else {
                        UserPreferences.SortOrder.BY_PRIORITY
                    }
                } else {
                    if (currentOrder == UserPreferences.SortOrder.BY_DEADLINE_AND_PRIORITY) {
                        UserPreferences.SortOrder.BY_DEADLINE
                    } else {
                        UserPreferences.SortOrder.NONE
                    }
                }
            preferences.toBuilder()
                .setSortOrder(newSortOrder)
                .build()
        }
    }

    suspend fun saveNewTask(task: Task) {
        dataStore.updateData { preferences ->
            preferences.toBuilder()
                .addTasks(task)
                .build()
        }
    }
}