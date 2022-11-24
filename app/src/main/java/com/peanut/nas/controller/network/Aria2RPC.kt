package com.peanut.nas.controller.network

import com.peanut.nas.controller.data.Aria2Result
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Aria2RPC {

    companion object {
        private var apiService: Aria2RPC? = null

        fun getInstance(baseUrl: String = "http://192.168.211.208:6800/"): Aria2RPC {
            if (apiService == null) {
                synchronized(Aria2RPC::class.java) {
                    if (apiService == null) {
                        apiService = Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(Aria2RPC::class.java)
                    }
                }
            }
            return apiService!!
        }
    }

    @GET("jsonrpc")
    suspend fun jsonrpc(
        @Query("params") params: String,
        @Query("id") id: Int,
        @Query("method") method: String,
        @Query("tm") tm: Long = System.currentTimeMillis()
    ): Aria2Result
}