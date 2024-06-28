package com.example.javawebsocketclient

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.javawebsocketclient.dto.RoomDto
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class create_room : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_room)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    fun create(view: View){
        val URL = "http://10.0.2.2:8080/api/v1/room"
        var roomName = findViewById<EditText>(R.id.roomNameEditText).text.toString();

        val stringRequest = object : StringRequest(
            Request.Method.POST, URL,
            Response.Listener<String> { response ->
                println("Ответ: $response")
                var roomId = Json.decodeFromString<RoomDto>(response).roomId.toString()

                val newIntent = Intent(this, room::class.java)
                val username = intent.getStringExtra("username")
                newIntent.putExtra("room", roomName)
                newIntent.putExtra("roomId", roomId)
                newIntent.putExtra("username", username)
                startActivity(newIntent)

            },
            Response.ErrorListener { error ->
                println("Ошибка: ${error.localizedMessage}")
            }
        ) {
            override fun getBody(): ByteArray {
                return roomName.toByteArray(Charsets.UTF_8)
            }
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/plain"
                return headers
            }
        }
        Volley.newRequestQueue(this).add(stringRequest);




    }


    fun leaveBacke(view: View){
        val intent = Intent(this, activity_list_rooms::class.java)
        startActivity(intent)
    }
}