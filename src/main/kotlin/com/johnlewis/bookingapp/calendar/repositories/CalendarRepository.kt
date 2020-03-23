package com.johnlewis.bookingapp.calendar.repositories

import com.johnlewis.bookingapp.calendar.domain.Room
import com.johnlewis.bookingapp.calendar.domain.RoomEvent
import com.johnlewis.bookingapp.calendar.domain.RoomEventRequest

interface CalendarRepository {

    fun getAllRoomCalendars(): List<Room>
    fun getRoomCalendar(roomId: String): Room
    fun getEventsForRoomCalendar(roomId: String): List<RoomEvent>
    fun createRoomEvent(roomEventRequest: RoomEventRequest) : String
    fun getRoomMotion(roomId: String): Boolean

}