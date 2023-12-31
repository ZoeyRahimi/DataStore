package com.deloitte.mcd.dojo.datastore.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deloitte.mcd.dojo.datastore.model.data.Task
import com.deloitte.mcd.dojo.datastore.model.tasks
import com.deloitte.mcd.dojo.datastore.ui.theme.TasksTheme
import java.text.SimpleDateFormat
import java.util.Locale

private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)

@Composable
fun TaskCard(task: Task) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(modifier = Modifier.background(colorResource(id = task.taskBackgroundColor()))) {
            Row {
                Column(Modifier.padding(8.dp)) {
                    Text(
                        text = task.name,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = MaterialTheme.colors.onSurface,
                    )
                    Text(
                        text = task.priority.name,
                        style = MaterialTheme.typography.body2.copy(
                            color = colorResource(id = task.taskTextColor())
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            tint = Color.Gray,
                            contentDescription = "Deadline",
                        )
                        Text(
                            text = dateFormat.format(task.deadline),
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, name = "Light mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark mode")
@Composable
fun PlanetCardPreview() {
    TasksTheme {
        TaskCard(tasks[0])
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, name = "Light mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark mode")
@Composable
fun PlanetCard1Preview() {
    TasksTheme {
        TaskCard(tasks[0])
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, name = "Light mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark mode")
@Composable
fun PlanetCard2Preview() {
    TasksTheme {
        TaskCard(tasks[0])
    }
}