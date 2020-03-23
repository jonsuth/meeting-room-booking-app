package com.johnlewis.bookingapp.calendar.model

import com.johnlewis.bookingapp.calendar.domain.Room

data class RoomModel(val name: String,
                     val floorNumber: Int,
                     val roomNumber: Int,
                     val id: String,
                     val slots: List<SlotModel>,
                     val capacity: String,
                     val equipment: List<String>) {
    companion object {
        fun build(room: Room): RoomModel {
            return RoomModel(
                    name = room.name,
                    floorNumber = room.floorNumber,
                    roomNumber = room.roomNumber,
                    id = room.id,
                    slots = SlotModel.build(room.events, room.hasMotion),
                    capacity = room.capacity.toString(),
                    equipment = room.equipment
            )
        }
    }
}