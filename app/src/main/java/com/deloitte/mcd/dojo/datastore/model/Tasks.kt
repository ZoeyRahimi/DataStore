package com.deloitte.mcd.dojo.datastore.model

import com.deloitte.mcd.dojo.datastore.model.data.Task
import com.deloitte.mcd.dojo.datastore.model.data.TaskPriority
import java.text.SimpleDateFormat
import java.util.Locale

private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

val tasks = listOf(
    Task(
        name = "Adding dependencies",
        deadline = simpleDateFormat.parse("2022-10-06")!!,
        priority = TaskPriority.LOW,
        completed = true
    ),
    Task(
        name = "Import project",
        deadline = simpleDateFormat.parse("2022-11-13")!!,
        priority = TaskPriority.MEDIUM,
        completed = true
    ),
    Task(
        name = "Creating the DataStore",
        deadline = simpleDateFormat.parse("2022-08-06")!!,
        priority = TaskPriority.LOW
    ),
    Task(
        name = "Read about DataStore",
        deadline = simpleDateFormat.parse("2022-12-03")!!,
        priority = TaskPriority.HIGH
    ),
    Task(
        name = "Add the data class",
        deadline = simpleDateFormat.parse("2022-09-06")!!,
        priority = TaskPriority.MEDIUM
    ),
    Task(
        name = "Reading data from Preferences DataStore",
        deadline = simpleDateFormat.parse("2022-07-03")!!,
        priority = TaskPriority.HIGH
    ),
    Task(
        name = "Handling exceptions while reading data",
        deadline = simpleDateFormat.parse("2022-06-03")!!,
        priority = TaskPriority.HIGH
    ),
    Task(
        name = "Writing data to Preferences DataStore",
        deadline = simpleDateFormat.parse("2022-05-03")!!,
        priority = TaskPriority.HIGH
    ),
    Task(
        name = "Migrating from SharedPreferences",
        deadline = simpleDateFormat.parse("2022-04-03")!!,
        priority = TaskPriority.HIGH
    ),
    Task(
        name = "Update TasksViewModel to use UserPreferencesRepository",
        deadline = simpleDateFormat.parse("2022-03-03")!!,
        priority = TaskPriority.HIGH
    ),
    Task(
        name = "Clean up SharedPreferencesRepository",
        deadline = simpleDateFormat.parse("2022-02-03")!!,
        priority = TaskPriority.HIGH
    )
)