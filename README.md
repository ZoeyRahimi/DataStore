TODO:

1. Add the dependency

       implementation "androidx.datastore:datastore-preferences:1.0.0"

3. Add the data class:

        data class UserPreferences(
            val showCompleted: Boolean,
            val sortOrder: SortOrder
        )

4. Creating the data store in AppModule

       @Provides
       @Singleton
        fun providePreferenceDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
            PreferenceDataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() }),
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES_NAME) }
            )

5. Add the repo

       class PreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

6. Add the Preference keys

       private object PreferencesKeys {
          val SHOW_COMPLETED = booleanPreferencesKey("show_completed")
          val SORT_ORDER = stringPreferencesKey("sort_order")
        }

7. Reading data from Preferences DataStore

       val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        }.map { preferences ->
            // Get our show completed value, defaulting to false if not set:
            val showCompleted = preferences[PreferencesKeys.SHOW_COMPLETED] ?: false
            val sortOrder =
                SortOrder.valueOf(
                    preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
                )
            UserPreferences(showCompleted, sortOrder)
        }

8. Add Error handling

       .catch { exception ->
              // dataStore.data throws an IOException when an error is encountered when reading data
              if (exception is IOException) {
                  emit(emptyPreferences())
              } else {
                  throw exception
              }

9. Writing data from Preferences DataStore

       suspend fun updateShowCompleted(showCompleted: Boolean) {
          dataStore.edit { preferences ->
              preferences[PreferencesKeys.SHOW_COMPLETED] = showCompleted
          }
        }

       suspend fun enableSortByDeadline(enable: Boolean) {
          // edit handles data transactionally, ensuring that if the sort is updated at the same
          // time from another thread, we won't have conflicts
          dataStore.edit { preferences ->
              // Get the current SortOrder as an enum
              val currentOrder = SortOrder.valueOf(
                  preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
              )

            val newSortOrder =
                if (enable) {
                    if (currentOrder == SortOrder.BY_PRIORITY) {
                        SortOrder.BY_DEADLINE_AND_PRIORITY
                    } else {
                        SortOrder.BY_DEADLINE
                    }
                } else {
                    if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
                        SortOrder.BY_PRIORITY
                    } else {
                        SortOrder.NONE
                    }
                }
            preferences[PreferencesKeys.SORT_ORDER] = newSortOrder.name
        }
        }

        suspend fun enableSortByPriority(enable: Boolean) {
            dataStore.edit { preferences ->
                val currentOrder = SortOrder.valueOf(
                    preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
                )

            val newSortOrder =
                if (enable) {
                    if (currentOrder == SortOrder.BY_DEADLINE) {
                        SortOrder.BY_DEADLINE_AND_PRIORITY
                    } else {
                        SortOrder.BY_PRIORITY
                    }
                } else {
                    if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
                        SortOrder.BY_DEADLINE
                    } else {
                        SortOrder.NONE
                    }
                }
            preferences[PreferencesKeys.SORT_ORDER] = newSortOrder.name
        }
        }


10. Migrate from sharedPreference

        migrations = listOf(SharedPreferencesMigration(context, USER_PREFERENCES_NAME)),

11. Update MainViewModel to use New repo

             private val userPreferencesFlow = preferencesRepository.userPreferencesFlow
       
           val tasksUiModelFlow = combine(
               tasksRepository.getTasks(),
               userPreferencesFlow
           ) { tasks: List<Task>, userPreferences: UserPreferences ->
               return@combine TasksUiModel(
                   tasks = filterSortTasks(
                       tasks,
                       userPreferences.showCompleted,
                       userPreferences.sortOrder
                   ),
                   showCompleted = userPreferences.showCompleted,
                   sortOrder = userPreferences.sortOrder
               )
           }

And:

                 fun enableSortByDeadline(enable: Boolean) {
        viewModelScope.launch {
            preferencesRepository.enableSortByDeadline(enable)
        }
    }

    fun showCompletedTasks(show: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updateShowCompleted(showCompleted = show)
        }
    }

    fun enableSortByPriority(enable: Boolean) {
        viewModelScope.launch {
            preferencesRepository.enableSortByPriority(enable)
        }
    }

11. Clean up the sharedPrefernce 
