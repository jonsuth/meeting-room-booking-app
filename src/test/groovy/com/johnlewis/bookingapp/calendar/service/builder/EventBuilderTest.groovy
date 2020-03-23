package com.johnlewis.bookingapp.calendar.service.builder

import com.johnlewis.bookingapp.calendar.domain.RoomEventRequest
import spock.lang.Specification

class EventBuilderTest extends Specification {

    def "should build Event from room event request"() {
        given:
        def request = new RoomEventRequest("Test event", "2018-03-19T08:45:00.000", "2018-03-19T09:45:00.000", "1.1@id.com", "")

        when:
        def event = new EventBuilder.Companion().build(request)

        then:
        event.getSummary() == "Test event"
        event.getStart().getDateTime().toString() == "2018-03-19T08:45:00.000Z"
        event.getEnd().getDateTime().toString() == "2018-03-19T09:45:00.000Z"
    }
}
