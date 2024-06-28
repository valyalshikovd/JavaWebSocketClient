package com.example.javawebsocketclient.dto


import kotlinx.serialization.Serializable
@Serializable
data class TableCardPlace(
    val downCard: Card,
    val upCard: Card
)
