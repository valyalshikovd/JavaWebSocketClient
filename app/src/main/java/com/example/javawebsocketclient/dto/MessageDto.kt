package com.example.javawebsocketclient.dto

import dev.gustavoavila.websocketclient.model.Payload
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto (
    var room: String? = null,
    var userName: String? = null,
    var command: String? = null,
    var payload: String? = null
)


