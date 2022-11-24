package com.peanut.nas.controller.network

import android.util.Base64
import com.peanut.nas.controller.data.Aria2Result

//https://aria2.github.io/manual/en/html/aria2c.html#methods
class Aria2Client private constructor(private val token: String) {
    private var requestId = 0

    companion object {
        private var client: Aria2Client? = null
        private var rpc: Aria2RPC? = null

        fun getInstance(baseUrl: String = "", token: String = ""): Aria2Client {
            if (client == null) {
                synchronized(Aria2Client::class.java) {
                    if (client == null) {
                        client = Aria2Client(token)
                        rpc = Aria2RPC.getInstance(baseUrl)
                    }
                }
            }
            return client!!
        }
    }

    private fun String.encodeBased64(): String = Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)

    suspend fun tellActive(): Aria2Result? {
        return rpc?.jsonrpc(id = ++requestId, method = "aria2.tellActive", params = arrayOf("token:$token").contentString().encodeBased64())
    }

    suspend fun tellStopped(size: Int = 1000): Aria2Result? {
        return rpc?.jsonrpc(id = ++requestId, method = "aria2.tellStopped", params = arrayOf("token:$token", 0, size).contentString().encodeBased64())
    }

    suspend fun tellWaiting(size: Int = 1000): Aria2Result? {
        return rpc?.jsonrpc(id = ++requestId, method = "aria2.tellWaiting", params = arrayOf("token:$token", 0, size).contentString().encodeBased64())
    }

    private fun<T> Array<T>.contentString(): String {
        val iMax = this.size - 1
        if (iMax == -1) return "[]"
        val b = StringBuilder()
        b.append('[')
        var i = 0
        while (true) {
            when(this[i]){
                is String -> b.append("\"${this[i]}\"")
                else -> b.append(this[i].toString())
            }
            if (i == iMax) return b.append(']').toString()
            b.append(", ")
            i++
        }
    }
}
