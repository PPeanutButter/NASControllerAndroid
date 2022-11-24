package com.peanut.nas.controller.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peanut.nas.controller.RequestStore
import com.peanut.nas.controller.SharedViewModel
import com.peanut.nas.controller.data.Aria2Task

@Composable
fun RepeatRequest(sharedViewModel: SharedViewModel) {
    if (sharedViewModel.activeTasks.value is RequestStore.Empty)
        sharedViewModel.getAllAria2Tasks()
    Column {
        ActiveTasks {
            val r = sharedViewModel.activeTasks.value
            if (r is RequestStore.Success) {
                LazyColumn {
                    items(items = r._datas, key = { task: Aria2Task -> task.gid.hashCode() }) {
                        Aria2Item(task = it, onSelected = { _, _ -> })
                    }
                }
            }
        }
        OtherTasks {
            val other = sharedViewModel.otherTasks.value
            if (other is RequestStore.Success) {
                LazyColumn {
                    items(items = other._datas, key = { task: Aria2Task -> task.gid.hashCode() }) {
                        Aria2Item(task = it, onSelected = { _, _ -> })
                    }
                }
            }
        }
    }

}

@Composable
fun ActiveTasks(content: @Composable () -> Unit) {
    Column {
        Text(text = "Active Tasks")
        SeparateLine()
        content()
        SeparateLine()
    }
}

@Composable
fun SeparateLine(){
    Spacer(modifier = Modifier
        .height(1.dp).fillMaxWidth().background(Color.Gray))
}

@Composable
fun OtherTasks(content: @Composable () -> Unit) {
    Column {
        Text(text = "Other Tasks")
        SeparateLine()
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun ActiveTasksPreview() {
    ActiveTasks {

    }
}
