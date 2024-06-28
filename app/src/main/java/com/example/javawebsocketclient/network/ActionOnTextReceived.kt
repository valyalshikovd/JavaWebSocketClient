package com.example.javawebsocketclient.network


fun interface ActionOnTextReceived {
    fun execute(s: String)
}
