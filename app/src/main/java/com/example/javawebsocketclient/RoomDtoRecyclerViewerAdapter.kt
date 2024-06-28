package com.example.javawebsocketclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.javawebsocketclient.databinding.RoomItemBinding
import com.example.javawebsocketclient.dto.RoomDto

class RoomDtoRecyclerViewerAdapter : RecyclerView.Adapter<RoomDtoRecyclerViewerAdapter.RoomDtoViewHolder>() {
    var roomList = ArrayList<RoomDto>()

    private var listener: OnItemClickListener? = null;

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    var data: List<RoomDto> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class RoomDtoViewHolder(val item: View, private val listener: OnItemClickListener?) : RecyclerView.ViewHolder(item){
        val binding = RoomItemBinding.bind(item)
        fun bind(room: RoomDto) = with(binding){
            name.text = room.roomName
            playerCounter.text = room.countPlayer.toString()
            status.text = room.status
        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    listener?.onItemClick(position)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomDtoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)
        return RoomDtoViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    override fun onBindViewHolder(holder: RoomDtoViewHolder, position: Int) {
        holder.bind(roomList[position])
    }

    fun addRoom(room: RoomDto){
        roomList.add(room)
        notifyDataSetChanged()
    }

    fun addRoomList(newRoomList: List<RoomDto>){
        roomList = newRoomList as ArrayList<RoomDto>;
        notifyDataSetChanged()
    }
}