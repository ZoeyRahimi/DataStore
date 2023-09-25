package com.deloitte.mcd.dojo.datastore.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject


class TasksRepository @Inject constructor(private val sharedPreferenceRepository: SharedPreferenceRepository) {

    private val _sortOrderFlow = MutableStateFlow(sharedPreferenceRepository.getSortOrder())
    val sortOrderFlow: StateFlow<SortOrder> = _sortOrderFlow

    fun enableSortByDeadline(enable: Boolean) {
        val currentOrder = sortOrderFlow.value
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
        sharedPreferenceRepository.updateSortOrder(newSortOrder)
        _sortOrderFlow.value = newSortOrder
    }

    fun enableSortByPriority(enable: Boolean) {
        val currentOrder = sortOrderFlow.value
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
        sharedPreferenceRepository.updateSortOrder(newSortOrder)
        _sortOrderFlow.value = newSortOrder
    }


    fun getTasks() = flowOf(tasks)
}