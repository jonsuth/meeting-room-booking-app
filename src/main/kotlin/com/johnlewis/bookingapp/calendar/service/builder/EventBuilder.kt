package com.johnlewis.bookingapp.calendar.service.builder

import com.google.api.client.util.DateTime
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.Event.Reminders
import com.google.api.services.calendar.model.EventAttendee
import com.google.api.services.calendar.model.EventDateTime
import com.johnlewis.bookingapp.calendar.domain.RoomEventRequest

class EventBuilder {
    companion object {

        private val EMAIL_DOMAIN = "@johnlewis.co.uk"

        fun build(roomEventRequest: RoomEventRequest): Event {
            return Event().setSummary(roomEventRequest.eventName)
                    .setStart(buildDateTime(roomEventRequest.startDate!!))
                    .setEnd(buildDateTime(roomEventRequest.endDate!!))
                    .setAttendees(buildAttendees(listOf(roomEventRequest.creatorEmail!!)))
                    .setReminders(buildReminders())
        }

        private fun buildDateTime(dateTime: String): EventDateTime {
            val offsetDateTime = DateTime(dateTime + "Z")
            return EventDateTime()
                    .setDateTime(offsetDateTime)
                    .setTimeZone("Europe/London")
        }

        private fun buildAttendees(attendees: List<String>): List<EventAttendee> {
            return attendees.map { EventAttendee().setEmail(it + EMAIL_DOMAIN) }
        }

        private fun buildReminders(): Reminders {
            return Reminders()
                    .setUseDefault(true)
        }
    }
}