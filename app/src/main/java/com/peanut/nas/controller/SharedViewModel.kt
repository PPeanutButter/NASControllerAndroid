package com.peanut.nas.controller

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peanut.nas.controller.data.Aria2DownloadState
import com.peanut.nas.controller.data.Aria2Task
import com.peanut.nas.controller.network.Aria2Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SharedViewModel: ViewModel() {
    var activeTasks: MutableState<RequestStore<Aria2Task>> = mutableStateOf(RequestStore.Empty())
    var otherTasks: MutableState<RequestStore<Aria2Task>> = mutableStateOf(RequestStore.Empty())
    private val client = Aria2Client.getInstance(baseUrl = "http://192.168.211.208:6800/", token = "0930")

    fun getAllAria2Tasks(){
        viewModelScope.launch(Dispatchers.IO) {
            while (true){
                try {
                    client.tellActive()?.result?.let {
                        //all task is active
                        activeTasks.value = RequestStore.Success(it.toMutableList())
                    }
                    val other = mutableListOf<Aria2Task>()
                    //tasks are either waiting or paused
                    client.tellWaiting()?.result?.let { other.addAll(it) }
                    //tasks are either complete or error
                    client.tellStopped()?.result?.let { other.addAll(it) }
                    other.sortBy {
                        it.state.priority
                    }
                    otherTasks.value = RequestStore.Success(other)
                } catch (e: Exception) {
                    e.printStackTrace()
                    activeTasks.value = RequestStore.Failure(e.localizedMessage ?: "unknown error")
                    otherTasks.value = RequestStore.Failure(e.localizedMessage ?: "unknown error")
                }
                delay(1000L)
            }
        }
    }
}