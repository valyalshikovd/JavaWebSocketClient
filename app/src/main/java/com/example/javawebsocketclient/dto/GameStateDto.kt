package com.example.javawebsocketclient.dto


import kotlinx.serialization.Serializable
@Serializable
data class GameStateDto(
    val table: List<TableCardPlace>,
    val playerCards: List<Card>,
    val trump: Card,
    val currentPlayer: Boolean,
    val deffencePlayer: Boolean,
    val countCardsOnTable: Int,
    val areThereAnyUnbrokenCards: Boolean,
    val countCardsInStack: Int,
    val draw: Boolean,
    val isGameOver: Boolean,
    val isWinner: Boolean,
    val countCardAtOpp: Int
)
