package com.peanut.nas.controller.data

import androidx.annotation.Keep
import com.peanut.nas.controller.R
@Keep
enum class Aria2DownloadState(val state: String, val priority: Int) {
    ACTIVE("active", 0),
    WAITING("waiting", 1),
    PAUSED("paused", 2),
    ERROR("error", 3),
    COMPLETE("complete", 4);

    companion object{
        fun parse(state: String): Aria2DownloadState{
            return when(state){
                ACTIVE.state -> ACTIVE
                WAITING.state -> WAITING
                PAUSED.state -> PAUSED
                ERROR.state -> ERROR
                COMPLETE.state -> COMPLETE
                else -> {
                    throw Exception("unable to parse $state")
                }
            }
        }
    }

}