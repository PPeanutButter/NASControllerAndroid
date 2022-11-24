package com.peanut.nas.controller

sealed class RequestStore<T>{
    class Success<T>() : RequestStore<T>(){
        var _datas: MutableList<T> = mutableListOf()
        var _data: T? = null

        constructor(data: MutableList<T>) : this(){
            this._datas = data
        }
        constructor(data: T) : this(){
            this._data = data
        }
    }
    class Failure<T>(val message: String) : RequestStore<T>()
    class Loading<T> : RequestStore<T>()
    class Empty<T> : RequestStore<T>()
}