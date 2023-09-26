package com.deloitte.mcd.dojo.datastore.ui

import androidx.lifecycle.ViewModel
import com.deloitte.mcd.dojo.datastore.model.SharedPreferenceRepository
import com.deloitte.mcd.dojo.datastore.model.SortOrder
import com.deloitte.mcd.dojo.datastore.model.TasksRepository
import com.deloitte.mcd.dojo.datastore.model.data.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tasksRepository: TasksRepository,
    private val sharedPreferenceRepository: SharedPreferenceRepository
) : ViewModel() {

    private val showCompletedFlow = MutableStateFlow(false)
    private val sortOrderFlow = sharedPreferenceRepository.sortOrderFlow

    val tasksUiModelFlow = combine(
        tasksRepository.getTasks(),
        showCompletedFlow,
        sortOrderFlow
    ) { tasks: List<Task>, showCompleted: Boolean, sortOrder: SortOrder ->
        return@combine TasksUiModel(
            tasks = filterSortTasks(tasks, showCompleted, sortOrder),
            showCompleted = showCompleted,
            sortOrder = sortOrder
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
        sharedPreferenceRepository.enableSortByDeadline(enable)
    }

    fun showCompletedTasks(show: Boolean) {
        showCompletedFlow.value = show
    }

    fun enableSortByPriority(enable: Boolean) {
        sharedPreferenceRepository.enableSortByPriority(enable)
    }
}

data class TasksUiModel(
    val tasks: List<Task>,
    val showCompleted: Boolean,
    val sortOrder: SortOrder
)