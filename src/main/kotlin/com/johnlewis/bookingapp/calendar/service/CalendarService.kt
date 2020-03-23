package com.johnlewis.bookingapp.calendar.service

import com.google.api.client.util.DateTime
import com.google.api.services.calendar.model.Calendar
import com.google.api.services.calendar.model.CalendarListEntry
import com.google.api.services.calendar.model.Event
import com.johnlewis.bookingapp.calendar.domain.RoomEventRequest
import com.johnlewis.bookingapp.calendar.service.builder.EventBuilder
import com.johnlewis.bookingapp.google_calendar.GoogleCalendarService
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class CalendarService(googleCalendarService: GoogleCalendarService) {

    private val calendar = googleCalendarService.getCalendarService()

    fun getAllRoomCalendars(): List<CalendarListEntry> {
        val calendarList = calendar.CalendarList()
                .list()
                .setPageToken(null)
                .execute()

        return calendarList.items
    }

    fun getRoomCalendar(roomId: String): Calendar {
        return calendar.Calendars()
                .get(roomId)
                .execute()
    }

    fun getEventsForRoomCalendar(calendarID: String): List<Event> {
        val start = Instant.now().toString().substring(0, 10) + "T08:00:00+00:00"
        val end = Instant.now().toString().substring(0, 10) + "T17:30:00+00:00"

        val events = calendar.events()
                .list(calendarID)
                .setSingleEvents(true)
                .setShowDeleted(false)
                .setTimeMin(DateTime(start))
                .setTimeMax(DateTime(end))
                .execute()

        return events.items
    }

    fun createEventForRoomCalendar(roomEventRequest: RoomEventRequest): String {
        val event = calendar.events()
                .insert(roomEventRequest.roomId, EventBuilder.build(roomEventRequest))
                .execute()

        return event.htmlLink
    }
}