package com.johnlewis.bookingapp.calendar.repositories

import com.johnlewis.bookingapp.calendar.domain.Room
import com.johnlewis.bookingapp.calendar.domain.RoomEvent
import com.johnlewis.bookingapp.calendar.domain.RoomEventRequest
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class StubCalendarRepository : CalendarRepository {
    override fun getAllRoomCalendars(): List<Room> {
        return listOf(
                Room("1.1@id.com", "1.1", 1,1,6, listOf(), "", listOf(), false),
                Room("1.2@id.com", "1.2", 1,2,6, listOf(), "", listOf(), false),
                Room("1.3@id.com", "1.3", 1,3,6, listOf(), "", listOf(), false),
                Room("1.4@id.com", "1.4", 1,4,6, listOf(), "", listOf(), false),
                Room("1.5@id.com", "1.5", 1,5,6, listOf(), "", listOf(), false),
                Room("1.6@id.com", "1.6", 1,6,6, listOf(), "", listOf(), false)
        )
    }

    override fun getRoomCalendar(roomId: String): Room {
        return Room("1.1@id.com", "1.1", 1,1,6, listOf(), "", listOf(), false)
    }

    override fun getEventsForRoomCalendar(roomId: String): List<RoomEvent> {
        return when (roomId) {
            "1.1@id.com" -> listOf(
                    RoomEvent(Instant.parse("2017-01-01T08:00:00Z"), Instant.parse("2017-01-01T10:00:00Z"), ""),
                    RoomEvent(Instant.parse("2017-01-01T12:00:00Z"), Instant.parse("2017-01-01T12:30:00Z"), ""),
                    RoomEvent(Instant.parse("2017-01-01T13:00:00Z"), Instant.parse("2017-01-01T14:00:00Z"), ""),
                    RoomEvent(Instant.parse("2017-01-01T16:00:00Z"), Instant.parse("2017-01-01T17:00:00Z"), "")
            )
            "1.2@id.com" -> listOf()
            "1.3@id.com" -> listOf()
            "1.4@id.com" -> listOf()
            "1.5@id.com" -> listOf()
            "1.6@id.com" -> listOf()
            else -> listOf()
        }
    }

    override fun createRoomEvent(roomEventRequest: RoomEventRequest): String {
        return ""
    }

    override fun getRoomMotion(roomId: String): Boolean {
        return true
    }
}