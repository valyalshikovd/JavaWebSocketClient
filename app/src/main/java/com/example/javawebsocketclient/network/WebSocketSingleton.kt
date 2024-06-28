package com.example.javawebsocketclient.network

import android.util.Log
import com.example.javawebsocketclient.dto.MessageDto
import dev.gustavoavila.websocketclient.WebSocketClient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.URISyntaxException

object WebSocketSingleton {

    private var webSocket: WebSocketClient? = null

    fun getInstance(roomId: String, userName: String, actionOnTextReceived: ActionOnTextReceived): WebSocketClient {
        println("Вызов getInstance")
        if (webSocket == null) {
            println("Создание сокета")
            val uri: URI = URI("ws://10.0.2.2:8080/websocket")

            webSocket = object : WebSocketClient(uri) {
                override fun onOpen() {


                    var message = MessageDto(roomId, userName, "connecting", "");
                    Log.i("WebSocket", "Session is starting")
                    (webSocket as WebSocketClient).send(
                        Json.encodeToString(
                            MessageDto.serializer(),
                            message
                        )
                    )

                    message = MessageDto(roomId, userName, "regUserInRoom", "");
                    (webSocket as WebSocketClient).send(
                        Json.encodeToString(
                            MessageDto.serializer(),
                            message
                        )
                    )
                }

                override fun onTextReceived(s: String) {
                    Log.i("WebSocket", "Message received")
                    println(actionOnTextReceived.toString())
                    actionOnTextReceived.execute(s)
                }

                override fun onBinaryReceived(data: ByteArray) {}
                override fun onPingReceived(data: ByteArray) {}
                override fun onPongReceived(data: ByteArray) {}
                override fun onException(e: Exception) {
                    Log.e("WebSocket", e.message!!)
                }

                override fun onCloseReceived(p0: Int, p1: String?) {
                    TODO("Not yet implemented")
                }

                fun onCloseReceived() {
                    Log.i("WebSocket", "Closed ")
                }

            }
            (webSocket as WebSocketClient).setConnectTimeout(10000)
            (webSocket as WebSocketClient).setReadTimeout(600000)
            (webSocket as WebSocketClient).enableAutomaticReconnection(5000)
            (webSocket as WebSocketClient).connect()
        }
        return webSocket!!
    }

    fun removeInstance(){
        webSocket = null;
    }

}