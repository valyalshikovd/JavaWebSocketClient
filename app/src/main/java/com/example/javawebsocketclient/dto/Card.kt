package com.example.javawebsocketclient.dto


import kotlinx.serialization.Serializable
@Serializable
data class Card(
    val suit: String,
    val rank: String
)
