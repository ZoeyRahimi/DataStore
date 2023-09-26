# DataStore

TODOs:

1. Add the dependencies:

       plugins {
                 ...
                 id "com.google.protobuf" version "0.9.1"
       }

        dependencies {
          //Proto dataStore
          implementation  "androidx.datastore:datastore:1.0.0"
          implementation  "com.google.protobuf:protobuf-javalite:3.18.0"
        }

        protobuf {
          protoc {
              artifact = "com.google.protobuf:protoc:3.19.4"
          }
      
          // Generates the java Protobuf-lite code for the Protobufs in this project. See
          // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
          // for more information.
          generateProtoTasks {
              all().each { task ->
                  task.builtins {
                      java {
                          option 'lite'
                      }
                  }
              }
          }
        }

2. Create the proto file

    - Create a new file called user_prefs.proto in the app/src/main/proto directory.

          syntax = "proto3";
       
          option java_package = "com.deloitte.mcd.dojo.datastore.model.data";
          option java_multiple_files = true;
       
          message UserPreferences {
            // filter for showing / hiding completed tasks
            bool show_completed = 1;
          }

3. Build the project and make sure the UserPreferences class is created

4. Create the serializer

        object UserPreferencesSerializer : Serializer<UserPreferences> {
            override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()
            override suspend fun readFrom(input: InputStream): UserPreferences {
                try {
                    return UserPreferences.parseFrom(input)
                } catch (exception: InvalidProtocolBufferException) {
                    throw CorruptionException("Cannot read proto.", exception)
                }
            }
        
            override suspend fun writeTo(t: UserPreferences, output: OutputStream) = t.writeTo(output)
        }

5. Creating the DataStore

    - The dataStore delegate:

            private const val USER_PREFERENCES_NAME = "user_preferences"
            private const val DATA_STORE_FILE_NAME = "user_prefs.pb"
            private const val SORT_ORDER_KEY = "sort_order"
         
            private val Context.userPreferencesStore: DataStore<UserPreferences> by dataStore(
                fileName = DATA_STORE_FILE_NAME,
                serializer = UserPreferencesSerializer
            )
         
            class MainActivity: AppCompatActivity() { ... }
    - Dependency Injection:

           @Provides
              @Singleton
              fun provideProtoDataStore(@ApplicationContext context: Context): DataStore<UserPreferences> =
                  DataStoreFactory.create(
                      serializer = UserPreferencesSerializer,
                      corruptionHandler = ReplaceFileCorruptionHandler { UserPreferences.getDefaultInstance() },
                      produceFile = { File(context.filesDir, USER_PREFERENCES_NAME) }
                  )

6. Create UserPreferencesRepository

       class UserPreferencesRepository @Inject constructor(private val dataStore: DataStore<UserPreferences>) {

7. Reading the preferences and Handling exceptions while reading data

       val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(UserPreferences.getDefaultInstance())
                } else {
                    throw exception
                }
            }

8. Now that everything works fine, let's add the sortOrder in proto file and remove the SortOrder
   kotlin class from the project

        message UserPreferences {
          // filter for showing / hiding completed tasks
          bool show_completed = 1;
        
          // defines tasks sorting order: no order, by deadline, by priority, by deadline and priority
          enum SortOrder {
            UNSPECIFIED = 0;
            NONE = 1;
            BY_DEADLINE = 2;
            BY_PRIORITY = 3;
            BY_DEADLINE_AND_PRIORITY = 4;
          }
        
          // user selected tasks sorting order
          SortOrder sort_order = 2;
        }

   Delete:

        enum class SortOrder {
            NONE,
            BY_DEADLINE,
            BY_PRIORITY,
            BY_DEADLINE_AND_PRIORITY
        }

9. clean up the code and fix the SortOrder imports

10. Migrating from SharedPreferences

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

11. Saving the sort order to DataStore

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

12. Now you can remove all the usages of SharedPreferences.