package com.example.javawebsocketclient

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.javawebsocketclient.dto.MessageDto
import com.example.javawebsocketclient.dto.RoomDto
import com.example.javawebsocketclient.network.ActionOnTextReceived
import com.example.javawebsocketclient.network.MessageProcessor
import com.example.javawebsocketclient.network.RunableMapSingleton
import com.example.javawebsocketclient.network.WebSocketSingleton
import dev.gustavoavila.websocketclient.WebSocketClient
import kotlinx.coroutines.flow.merge
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.URISyntaxException

class room() : AppCompatActivity() {

    private var webSocketClient: WebSocketClient? = null
    private var readyFlag: Boolean = false;



    init {
        RunableMapSingleton.get()["addNotification"] =
            MessageProcessor { messageDto -> addNotification(messageDto) }
        RunableMapSingleton.get()["start_game"] = MessageProcessor { messageDto -> startGame(messageDto) }
    }

    private fun addNotification(messageDto: MessageDto) {
        if (messageDto.payload != null)
            handleAddNotification(messageDto.payload!!)
    }

    fun handleAddNotification(string: String) {
        findViewById<TextView>(R.id.notification).text =
            findViewById<TextView>(R.id.notification).text.toString() + "\n " + string
    }

    private fun startGame(messageDto: MessageDto) {

        val room = intent.getStringExtra("roomName")
        val roomId = intent.getStringExtra("roomId")
        val userName = intent.getStringExtra("username")

        val newIntent = Intent(this, GameActivity::class.java)
        val username = intent.getStringExtra("username")
        newIntent.putExtra("room", room)
        newIntent.putExtra("roomId", roomId)
        newIntent.putExtra("username", username)

        startActivity(newIntent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_room)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val roomName = findViewById<TextView>(R.id.roomNameTextView)
        roomName.text = intent.getStringExtra("roomName").toString()
        findViewById<TextView>(R.id.readyFlagText).text = "Вы не готовы"
        createWebSocketClient()
    }


    private fun executeActionOnTextReceived(s: String) {
        runOnUiThread {
            try {
                val msg: MessageDto = Json.decodeFromString(s)
                val runnable =  RunableMapSingleton.get()[msg.command]
                if (runnable != null) {
                    runnable.process(msg)
                } else {
                    println("Действие не найдено: ${msg.command}")
                }
            } catch (e: Exception) {
                println("что-то не так")
                e.printStackTrace()
            }
        }
    }

    private fun createWebSocketClient() {

        webSocketClient = WebSocketSingleton.getInstance(
            intent.getStringExtra("roomId").toString(),
            intent.getStringExtra("username").toString(),
            ::executeActionOnTextReceived)
    }

    fun leaveButton(view: View){
        var roomId = intent.getStringExtra("roomId").toString()
        var userName = intent.getStringExtra("username").toString()
        var message = MessageDto(roomId, userName, "leaveFromRoom", "");
        (webSocketClient as WebSocketClient).send(
            Json.encodeToString(
                MessageDto.serializer(),
                message
            )
        )
        WebSocketSingleton.removeInstance()
        val newIntent = Intent(this, activity_list_rooms::class.java)
        val username = intent.getStringExtra("username")
        newIntent.putExtra("username", username)
        startActivity(newIntent)
    }

    fun readyButton(view: View) {
        readyFlag = !readyFlag;
        val room = intent.getStringExtra("roomName")
        val roomId = intent.getStringExtra("roomId")
        val userName = intent.getStringExtra("username")
        if (readyFlag) {




            findViewById<TextView>(R.id.readyFlagText).text =
                "Вы готовы, ожитайте готовность другого игрока"
            var message = MessageDto(roomId, userName, "readyToPlay", "");
            (webSocketClient as WebSocketClient).send(
                Json.encodeToString(
                    MessageDto.serializer(),
                    message
                )
            )
        } else {
            var message = MessageDto(roomId, userName, "unreadyToPlay", "");
            (webSocketClient as WebSocketClient).send(
                Json.encodeToString(
                    MessageDto.serializer(),
                    message
                )
            )
            findViewById<TextView>(R.id.readyFlagText).text = "Вы не готовы"
        }
    }
}