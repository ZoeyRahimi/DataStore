package com.deloitte.mcd.dojo.datastore.model

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

private const val SORT_ORDER_KEY = "sort_order"

class SharedPreferenceRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun getSortOrder(): SortOrder {
        val order = sharedPreferences.getString(SORT_ORDER_KEY, SortOrder.NONE.name)
            ?: SortOrder.NONE.name
        return SortOrder.valueOf(order)
    }


    fun updateSortOrder(sortOrder: SortOrder) {
        sharedPreferences.edit {
            putString(SORT_ORDER_KEY, sortOrder.name)
        }
    }
}