package com.example.javawebsocketclient

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.javawebsocketclient.databinding.ActivityListRoomsBinding
import com.example.javawebsocketclient.dto.RoomDto
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class activity_list_rooms : AppCompatActivity() {
    lateinit var binding: ActivityListRoomsBinding
    private val adapter = RoomDtoRecyclerViewerAdapter()


    private fun onClick(view: View, pos: Int){
        val clickedItem = adapter.roomList[pos]
        println(clickedItem.toString())

        val newIntent = Intent(this, room::class.java)
        val username = intent.getStringExtra("username")
        newIntent.putExtra("room", clickedItem.roomName)
        newIntent.putExtra("roomId", clickedItem.roomId)
        newIntent.putExtra("username", username)
        startActivity(newIntent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_rooms)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityListRoomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter.setOnItemClickListener(object : OnItemClickListener {

            override fun onItemClick(position: Int) {
                onClick(findViewById<View>(R.id.main), position)
            }
        })

        init()
        getAllRooms()

    }




    private fun init() = with(binding){

        list.layoutManager = LinearLayoutManager(this@activity_list_rooms )
        list.adapter = adapter
    }

    fun changeViewCreateRoom(view: View) {
        val newIntent = Intent(this, create_room::class.java)
        val userName = intent.getStringExtra("username")
        newIntent.putExtra("username", userName)
        startActivity(newIntent)
    }

    fun handleUpdateRooms(view: View){
        getAllRooms()
    }

    private fun getAllRooms(){
        val URL = "http://10.0.2.2:8080/api/v1/room"
        val stringRequest = StringRequest(
            Request.Method.GET, "$URL/getAll",
            { response ->
                println(Json.decodeFromString<List<RoomDto>>(response))

                adapter.addRoomList(Json.decodeFromString<List<RoomDto>>(response))

            },
            { error ->
                println("Ошибка: ${error.localizedMessage}")
            }
        )

        Volley.newRequestQueue(this).add(stringRequest)
    }
}