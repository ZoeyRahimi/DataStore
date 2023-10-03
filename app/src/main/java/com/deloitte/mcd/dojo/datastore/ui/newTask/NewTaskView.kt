package com.deloitte.mcd.dojo.datastore.ui.newTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deloitte.mcd.dojo.datastore.R
import com.deloitte.mcd.dojo.datastore.model.data.Task
import com.deloitte.mcd.dojo.datastore.model.data.TaskPriority
import com.deloitte.mcd.dojo.datastore.utils.DateUtils
import java.util.Date

@Composable
fun NewTaskView(onBackButtonClick: () -> Unit, onSaveButtonClick: (Task) -> Unit) {
    var isCompleted by remember { mutableStateOf(false) }
    var taskPriority by remember { mutableStateOf(TaskPriority.HIGH) }
    var taskTitle by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                navigationIcon = {
                    IconButton(onClick = onBackButtonClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                title = { Text(stringResource(R.string.app_name)) }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                Text(
                    text = "Create new task", modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.h1
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                        .background(
                            color = colorResource(id = R.color.lightGrey), // Background color
                            shape = RoundedCornerShape(8.dp) // Rounded corner shape
                        )
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.Transparent),
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        placeholder = { Text(text = "Task title") },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            disabledTextColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                }
            }
            item {
                Text(
                    text = "Priority",
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            item {
                SegmentedControl(onItemSelection = {
                    taskPriority = it
                })
            }
            item {
                Text(
                    text = "Deadline",
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            item {
                DatePicker { date = it }
            }
            item {
                Row {
                    Text(
                        text = "Completed",
                        style = MaterialTheme.typography.h2,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .weight(1f)
                    )
                    Checkbox(
                        checked = isCompleted,
                        onCheckedChange = { newValue -> isCompleted = newValue },
                        colors = CheckboxDefaults.colors(
                            checkedColor = colors.primary,
                            uncheckedColor = colors.primary,
                        )
                    )
                }
            }
            item {
                Row {
                    Button(
                        onClick = onBackButtonClick,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        shape = RoundedCornerShape(
                            topStartPercent = 10,
                            topEndPercent = 10,
                            bottomStartPercent = 10,
                            bottomEndPercent = 10
                        )
                    ) {
                        Text(text = "Cancel")
                    }

                    Button(
                        onClick = {
                            if (taskTitle.isNotEmpty())
                                onSaveButtonClick(
                                    Task.newBuilder()
                                        .setCompleted(isCompleted)
                                        .setDeadline(date)
                                        .setName(taskTitle)
                                        .setPriority(taskPriority)
                                        .build()
                                )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        shape = RoundedCornerShape(
                            topStartPercent = 10,
                            topEndPercent = 10,
                            bottomStartPercent = 10,
                            bottomEndPercent = 10
                        )
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}
