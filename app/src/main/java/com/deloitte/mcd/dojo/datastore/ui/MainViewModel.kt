package com.deloitte.mcd.dojo.datastore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deloitte.mcd.dojo.datastore.model.SortOrder
import com.deloitte.mcd.dojo.datastore.model.TasksRepository
import com.deloitte.mcd.dojo.datastore.model.UserPreferencesRepository
import com.deloitte.mcd.dojo.datastore.model.data.Task
import com.deloitte.mcd.dojo.datastore.model.data.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tasksRepository: TasksRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

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

    private fun filterSortTasks(
        tasks: List<Task>,
        showCompleted: Boolean,
        sortOrder: SortOrder
    ): List<Task> {
        // filter the tasks
        val filteredTasks = if (showCompleted) {
            tasks
        } else {
            tasks.filter { !it.completed }
        }
        // sort the tasks
        return when (sortOrder) {
            SortOrder.NONE -> filteredTasks
            SortOrder.BY_DEADLINE -> filteredTasks.sortedByDescending { it.deadline }
            SortOrder.BY_PRIORITY -> filteredTasks.sortedBy { it.priority }
            SortOrder.BY_DEADLINE_AND_PRIORITY -> filteredTasks.sortedWith(
                compareByDescending<Task> { it.deadline }.thenBy { it.priority }
            )
        }
    }

    fun enableSortByDeadline(enable: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.enableSortByDeadline(enable)
        }
    }

    fun showCompletedTasks(show: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateSHowCompleted(showCompleted = show)
        }
    }

    fun enableSortByPriority(enable: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.enableSortByPriority(enable)
        }
    }
}

data class TasksUiModel(
    val tasks: List<Task>,
    val showCompleted: Boolean,
    val sortOrder: SortOrder
)