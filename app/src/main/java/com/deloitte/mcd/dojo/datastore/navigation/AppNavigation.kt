package com.deloitte.mcd.dojo.datastore.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deloitte.mcd.dojo.datastore.ui.allTask.AllTasks
import com.deloitte.mcd.dojo.datastore.ui.MainViewModel
import com.deloitte.mcd.dojo.datastore.ui.newTask.NewTaskView


enum class AppScreens {
    AllTasks, NewTask
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<MainViewModel>()
    NavHost(navController = navController, startDestination = AppScreens.AllTasks.name) {
        composable(AppScreens.AllTasks.name) {
            AllTasks(viewModel) {
                navController.navigate(AppScreens.NewTask.name)
            }
        }
        composable(AppScreens.NewTask.name) {
            NewTaskView(onSaveButtonClick = { task ->
                Log.d("Test", task.toString())
                viewModel.addNewTask(task)
                navController.popBackStack()
            }, onBackButtonClick = {
                navController.popBackStack()
            })
        }
    }
}