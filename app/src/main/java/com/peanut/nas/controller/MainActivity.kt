package com.peanut.nas.controller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.peanut.nas.controller.data.Aria2DownloadState
import com.peanut.nas.controller.data.EXAMPLE_ACTIVE
import com.peanut.nas.controller.ui.component.Aria2Item
import com.peanut.nas.controller.ui.component.RepeatRequest
import com.peanut.nas.controller.ui.theme.NASControllerTheme

class MainActivity : ComponentActivity() {
    private val viewModel: SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NASControllerTheme {
                //A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    RepeatRequest(viewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NASControllerTheme {
        Column {
            Aria2Item(task = EXAMPLE_ACTIVE, onSelected = { _, _ -> })
        }
    }
}