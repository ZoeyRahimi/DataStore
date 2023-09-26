package com.deloitte.mcd.dojo.datastore.model.data

import com.deloitte.mcd.dojo.datastore.R
import java.util.Date

enum class TaskPriority {
    HIGH, MEDIUM, LOW
}

data class Task(
    val name: String,
    val deadline: Date,
    val priority: TaskPriority,
    val completed: Boolean = false
) {
    fun taskTextColor(): Int {
        return when (this.priority) {
            TaskPriority.HIGH -> R.color.red
            TaskPriority.MEDIUM -> R.color.yellow
            TaskPriority.LOW -> R.color.green
        }
    }

    fun taskBackgroundColor(): Int {
        return when (this.completed) {
            true -> R.color.greyAlpha
            false -> R.color.white
        }
    }
}