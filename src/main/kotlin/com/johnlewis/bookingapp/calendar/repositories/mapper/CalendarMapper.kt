package com.johnlewis.bookingapp.calendar.repositories.mapper

import com.google.api.services.calendar.model.Calendar
import com.google.api.services.calendar.model.CalendarListEntry
import com.google.api.services.calendar.model.Event
import com.johnlewis.bookingapp.calendar.domain.Room
import com.johnlewis.bookingapp.calendar.domain.RoomEvent
import com.johnlewis.bookingapp.calendar.repositories.resources.ListOfValues
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.regex.Pattern

@Component
class CalendarMapper(private val listOfValues: ListOfValues) {

    private val ROOM_NAME_PATTERN = Pattern.compile("-([0-9]+[.][0-9]+) ")
    private val CAPACITY_PATTERN = Pattern.compile("\\((.*?)\\)")
    private val EQUIPMENT_PATTERN = Pattern.compile("\\[(.*?)\\]")

    fun mapCalendarListToRooms(calendars: List<CalendarListEntry>): List<Room> {
        return calendars
                .filter { listOfValues.roomIds.contains(it.id) }
                .map { buildRoom(it.id, it.summary) }
    }

    fun mapCalendarToRoom(roomCalendar: Calendar): Room {
        return buildRoom(roomCalendar.id, roomCalendar.summary)
    }

    fun mapToEvents(events: List<Event>): List<RoomEvent> {
        return events
                .filter { it.status == "confirmed" }
                .filter { it.start?.dateTime != null && it.end?.dateTime != null }
                .map {
                    RoomEvent(
                            startTime = Instant.ofEpochMilli(it.start.dateTime.value).plusSeconds(3600),
                            endTime = Instant.ofEpochMilli(it.end.dateTime.value).plusSeconds(3600),
                            eventName = it.summary ?: "PRIVATE"
                    )
                }
                .sortedBy { it.startTime }
    }

    private fun buildRoom(id: String, summary: String?): Room {
        return Room(
                id = id,
                name = patternFromSummary(ROOM_NAME_PATTERN, summary) ?: "UNKNOWN",
                floorNumber = Integer.parseInt(patternFromSummary(ROOM_NAME_PATTERN, summary)?.substringBefore(".") ?: "UNKNOWN"),
                roomNumber = Integer.parseInt(patternFromSummary(ROOM_NAME_PATTERN, summary)?.substringAfter(".") ?: "UNKNOWN"),
                capacity = patternFromSummary(CAPACITY_PATTERN, summary)?.toInt() ?: 0,
                equipment = patternFromSummary(EQUIPMENT_PATTERN, summary)?.split(",") ?: listOf("Information not available"),
                location = ""
        )
    }

    private fun patternFromSummary(pattern: Pattern, summary: String?): String? {
        val matcher = pattern.matcher(summary)

        if (matcher.find()) {
            return matcher.group(1)
        }

        return null
    }

}