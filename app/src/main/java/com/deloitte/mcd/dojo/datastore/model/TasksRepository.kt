package com.deloitte.mcd.dojo.datastore.model

import kotlinx.coroutines.flow.flowOf

class TasksRepository {
    fun getTasks() = flowOf(tasks)
}