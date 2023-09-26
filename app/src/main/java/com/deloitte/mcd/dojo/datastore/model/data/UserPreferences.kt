package com.deloitte.mcd.dojo.datastore.model.data

import com.deloitte.mcd.dojo.datastore.model.SortOrder

data class UserPreferences(
    val showCompleted: Boolean,
    val sortOrder: SortOrder
)
