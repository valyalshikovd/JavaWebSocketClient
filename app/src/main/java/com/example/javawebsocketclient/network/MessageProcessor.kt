package com.example.javawebsocketclient.network

import com.example.javawebsocketclient.dto.MessageDto

fun interface MessageProcessor {
    fun process(messageDto: MessageDto)
}
