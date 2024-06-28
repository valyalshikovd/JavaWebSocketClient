package com.example.javawebsocketclient

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.javawebsocketclient.dto.GameStateDto
import com.example.javawebsocketclient.dto.MessageDto
import com.example.javawebsocketclient.dto.RoomDto
import com.example.javawebsocketclient.network.MessageProcessor
import com.example.javawebsocketclient.network.RunableMapSingleton
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class GameActivity : AppCompatActivity() {


    init {
        RunableMapSingleton.get()["game_state"] =
            MessageProcessor { messageDto -> getState(messageDto) }
    }

    private fun getState(messageDto: MessageDto) {

        println(messageDto.payload)
        println(messageDto.payload?.let { Json.decodeFromString<GameStateDto>(it) }.toString())

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}