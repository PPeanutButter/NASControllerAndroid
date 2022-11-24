package com.peanut.nas.controller.ui.component

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peanut.nas.controller.R
import com.peanut.nas.controller.data.Aria2DownloadState
import com.peanut.nas.controller.data.Aria2Task
import com.peanut.nas.controller.data.EXAMPLE_ACTIVE

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Aria2Item(task: Aria2Task, onSelected: ((isSelected: Boolean, task: Aria2Task) -> Unit)? = null) {
    val context = LocalContext.current
    val state = task.state
    val stateLabel = task.state.state
    Box {
        var selected by remember { mutableStateOf(false) }
        LaunchedEffect(key1 = selected, block = {
            if (onSelected != null)
                onSelected(selected, task)
        })
        Checkbox(
            checked = selected, onCheckedChange = { selected = !selected }, modifier = Modifier
                .wrapContentSize()
                .clickable(interactionSource = MutableInteractionSource(),
                    indication = null, onClick = {})
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(onLongClick = {
                    Toast
                        .makeText(context, stateLabel, Toast.LENGTH_SHORT)
                        .show()
                }, onClick = {})
                .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .clickable { selected = !selected },
                text = task.files[0].getFileName(),
                fontSize = 20.sp,
                maxLines = 2,
                lineHeight = 24.sp,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    textIndent = TextIndent(32.sp)
                )
            )
            if (state == Aria2DownloadState.ACTIVE){
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Rounded.Downloading, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(text = "${task.downloadSize} / ${task.totalSize}")
                    Icon(imageVector = Icons.Rounded.Timelapse, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(text = task.remainTimeDesc)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                when (state) {
                    Aria2DownloadState.COMPLETE -> {
                        Icon(imageVector = Icons.Rounded.DownloadForOffline, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text(text = task.totalSize)
                    }
                    Aria2DownloadState.ERROR -> {
                        Icon(imageVector = Icons.Rounded.Error, tint = MaterialTheme.colorScheme.error, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text(text = task.errorMessage?:task.errorCodeMessage, color = MaterialTheme.colorScheme.error)
                    }
                    Aria2DownloadState.WAITING -> {
                        Icon(imageVector = Icons.Rounded.Schedule, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                    Aria2DownloadState.PAUSED -> {
                        Icon(imageVector = Icons.Rounded.Pause, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                    else -> {}
                }
                ProgressBar(height = 26.dp, progress = task.downloadPercent, state = state)
            }
            //https://fonts.google.com/icons?icon.style=Rounded&icon.query=download
            if (state == Aria2DownloadState.ACTIVE) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Rounded.Speed, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(text = task.downloadSpeedDesc)
                    Icon(imageVector = Icons.Rounded.SignalCellularAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(text = task.connections)
                }
            }
        }
    }
}

@Composable
fun ProgressBar(height: Dp = 8.dp, progress: Float, state: Aria2DownloadState) {
    var mProgress = progress
    val progressColor = when(state){
        Aria2DownloadState.ACTIVE -> MaterialTheme.colorScheme.primary
        Aria2DownloadState.ERROR -> MaterialTheme.colorScheme.error
        Aria2DownloadState.COMPLETE -> colorResource(id = R.color.aria2_download_success).also { mProgress = 1f }
        else -> Color.Gray
    }
    val backgroundColor = progressColor.copy(alpha = 0.15f)
    val corner = 16f
    Box {
        Surface(color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .drawBehind {
                    drawRoundRect(
                        color = backgroundColor,
                        cornerRadius = CornerRadius(corner)
                    )
                }) {
        }
        Surface(color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth(fraction = mProgress)
                .height(height)
                .drawBehind {
                    drawRoundRect(
                        color = progressColor,
                        cornerRadius = CornerRadius(corner)
                    )
                }) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                Text(text = String.format("%.2f %%", mProgress * 100), maxLines = 1, overflow = TextOverflow.Clip, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun Aria2ItemPreview() {
    Column {
        Aria2Item(task = EXAMPLE_ACTIVE, onSelected = { _, _ -> })
    }
}