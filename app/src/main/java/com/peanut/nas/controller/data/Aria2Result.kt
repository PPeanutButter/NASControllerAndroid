package com.peanut.nas.controller.data

import androidx.annotation.Keep

@Keep
data class Aria2Result(
    val id: Int,
    val jsonrpc: String,
    val result: List<Aria2Task>
)
