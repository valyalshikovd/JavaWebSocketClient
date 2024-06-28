package com.example.javawebsocketclient.dto

import kotlinx.serialization.Serializable


@Serializable
 data class ReceivedMessageDto (
     var command: String? = null,
     var payload: String? = null
 )