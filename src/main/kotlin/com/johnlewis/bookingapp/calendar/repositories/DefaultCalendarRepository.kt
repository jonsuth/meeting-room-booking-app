package com.johnlewis.bookingapp.calendar.repositories

import com.johnlewis.bookingapp.calendar.domain.Room
import com.johnlewis.bookingapp.calendar.domain.RoomEvent
import com.johnlewis.bookingapp.calendar.domain.RoomEventRequest
import com.johnlewis.bookingapp.calendar.repositories.mapper.CalendarMapper
import com.johnlewis.bookingapp.calendar.service.CalendarService
import com.johnlewis.bookingapp.calendar.service.SheetsService
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Primary
@Component
class DefaultCalendarRepository(private val calendarService: CalendarService,
                                private val calendarMapper: CalendarMapper,
                                private val sheetsService: SheetsService) : CalendarRepository {

    override fun getAllRoomCalendars(): List<Room> {
        return calendarMapper.mapCalendarListToRooms(calendarService.getAllRoomCalendars())
    }

    override fun getRoomCalendar(roomId: String): Room {
        return calendarMapper.mapCalendarToRoom(calendarService.getRoomCalendar(roomId))
    }

    override fun getEventsForRoomCalendar(roomId: String): List<RoomEvent> {
        return calendarMapper.mapToEvents(calendarService.getEventsForRoomCalendar(roomId))
    }

    override fun createRoomEvent(roomEventRequest: RoomEventRequest): String {
        return calendarService.createEventForRoomCalendar(roomEventRequest)
    }

    override fun getRoomMotion(roomId: String): Boolean {
        return sheetsService.getRoomMotion(roomId)
    }
}