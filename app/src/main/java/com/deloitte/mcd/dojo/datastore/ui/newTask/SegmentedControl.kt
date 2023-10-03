package com.deloitte.mcd.dojo.datastore.ui.newTask

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.deloitte.mcd.dojo.datastore.R
import com.deloitte.mcd.dojo.datastore.model.data.TaskPriority

@Composable
fun SegmentedControl(
    items: List<TaskPriority> = TaskPriority.values().filter { it != TaskPriority.UNRECOGNIZED },
    defaultSelectedItem: TaskPriority = TaskPriority.HIGH,
    onItemSelection: (selectedItem: TaskPriority) -> Unit
) {
    val selectedItem = remember { mutableStateOf(defaultSelectedItem) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .background(
                color = colorResource(id = R.color.lightGrey), // Background color
                shape = RoundedCornerShape(8.dp) // Rounded corner shape
            )
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            items.forEach { item ->
                val isSelectedItem = selectedItem.value == item
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .offset(0.dp, 0.dp)
                        .zIndex(if (isSelectedItem) 1f else 0f)
                        .padding(4.dp),
                    onClick = {
                        selectedItem.value = item
                        onItemSelection(selectedItem.value)
                    },
                    shape = RoundedCornerShape(
                        topStartPercent = 10,
                        topEndPercent = 10,
                        bottomStartPercent = 10,
                        bottomEndPercent = 10
                    ),
                    border = BorderStroke(
                        1.dp, if (isSelectedItem) {
                            colors.primary
                        } else {
                            colors.primary.copy(alpha = 0.75f)
                        }
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = if (isSelectedItem) {
                            colors.primary
                        } else Color.Transparent
                    )
                ) {
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.Normal,
                        color = if (isSelectedItem) {
                            Color.White
                        } else {
                            colors.primary.copy(alpha = 0.9f)
                        },
                    )
                }
            }
        }
    }
}