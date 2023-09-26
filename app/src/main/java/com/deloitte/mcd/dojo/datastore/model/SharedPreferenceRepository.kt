package com.deloitte.mcd.dojo.datastore.model

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

private const val SORT_ORDER_KEY = "sort_order"

class SharedPreferenceRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

    private val _sortOrderFlow = MutableStateFlow(getSortOrder())
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
        updateSortOrder(newSortOrder)
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
        updateSortOrder(newSortOrder)
        _sortOrderFlow.value = newSortOrder
    }

    private fun getSortOrder(): SortOrder {
        val order = sharedPreferences.getString(SORT_ORDER_KEY, SortOrder.NONE.name)
            ?: SortOrder.NONE.name
        return SortOrder.valueOf(order)
    }


    private fun updateSortOrder(sortOrder: SortOrder) {
        sharedPreferences.edit {
            putString(SORT_ORDER_KEY, sortOrder.name)
        }
    }
}