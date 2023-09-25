package com.deloitte.mcd.dojo.datastore.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deloitte.mcd.dojo.datastore.R

@Composable
fun AllTasks() {
    val viewModel: MainViewModel = viewModel()
    val state by viewModel.tasksUiModelFlow.collectAsState(initial = null)
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                title = { Text(stringResource(R.string.app_name)) }
            )
        },
    ) {
        Column {
            SwitchView(viewModel)
            LazyColumn(
                Modifier.fillMaxWidth(),
                contentPadding = it
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "\uD83D\uDDD3️  Tasks:",
                            style = MaterialTheme.typography.h1
                        )
                    }
                }
                state?.tasks?.let {
                    items(it) { task ->
                        TaskCard(task)
                    }
                }
            }
        }
    }
}

@Composable
fun SwitchView(viewModel: MainViewModel) {
    var switch1 by remember { mutableStateOf(false) }
    var switch2 by remember { mutableStateOf(false) }
    var switch3 by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "\uD83D\uDDC3️  Settings:", style = MaterialTheme.typography.h1)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Priority", modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )
            Switch(
                checked = switch1,
                onCheckedChange = { newChecked ->
                    switch1 = newChecked
                    viewModel.enableSortByPriority(newChecked)
                },
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Deadline", modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )
            Switch(
                checked = switch2,
                onCheckedChange = { newChecked ->
                    switch2 = newChecked
                    viewModel.enableSortByDeadline(newChecked)
                },
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Show Completed tasks", modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )
            Switch(
                checked = switch3,
                onCheckedChange = { newChecked ->
                    switch3 = newChecked
                    viewModel.showCompletedTasks(newChecked)
                },
            )
        }

    }
}