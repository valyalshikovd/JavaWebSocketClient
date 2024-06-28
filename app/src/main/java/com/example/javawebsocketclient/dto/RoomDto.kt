package com.example.javawebsocketclient.dto

import kotlinx.serialization.Serializable
@Serializable
data class RoomDto (
        var roomId: String? = null,
        var roomName: String? = null,
        var status: String? = null,
        var countPlayer: Int? = null
)

