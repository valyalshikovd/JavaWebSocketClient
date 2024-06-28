package com.example.javawebsocketclient.network

import com.example.javawebsocketclient.room

object RunableMapSingleton {

    private val runnableMap: MutableMap<String, MessageProcessor> = mutableMapOf()
    fun get(): MutableMap<String, MessageProcessor>{
        return runnableMap
    }
}