package com.johnlewis.bookingapp.calendar.model

import com.johnlewis.bookingapp.calendar.domain.Room

data class CalendarModel(val rooms: List<RoomModel>) {
    companion object {
        fun build(rooms: List<Room>): CalendarModel {
            val roomModels = rooms
                    .map { RoomModel.build(it) }
                    .sortedWith(compareBy({ it.floorNumber }, { it.roomNumber }))
            return CalendarModel(roomModels)
        }
    }
}




