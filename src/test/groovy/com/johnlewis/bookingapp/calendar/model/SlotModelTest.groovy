package com.johnlewis.bookingapp.calendar.model

import com.johnlewis.bookingapp.calendar.domain.RoomEvent
import spock.lang.Specification

import java.time.Instant

class SlotModelTest extends Specification {

    def "should build a EMPTY slot model when there are no room events"() {
        when:
        def slots = new SlotModel.Companion().build([], false)

        then:
        slots == []
    }

    def "should build a SINGLE event slot model correctly"() {
        given: "A single room event between 10:00 and 16:00"
        def roomEvents = [new RoomEvent(buildInstantGivenTime("10:00"), buildInstantGivenTime("16:00"), "Event name")]

        when:
        def slots = new SlotModel.Companion().build(roomEvents, false)

        then: "Slot model should be as follows: FREE 08:00-10:00 | EVENT 10:00-16:00 | FREE 16:00-17:59"
        slots.size() == 3

        slots[0].startTime == "T08:00:00Z"
        slots[0].endTime == "T10:00:00Z"

        slots[1].startTime == "T10:00:00Z"
        slots[1].endTime == "T16:00:00Z"

        slots[2].startTime == "T16:00:00Z"
        slots[2].endTime == "T17:54:00Z"
    }

    def "should build a MULTI event slot model with BACK TO BACK events and GAPS between events correctly"() {
        given: "Multiple room events"
        def roomEvents = [new RoomEvent(buildInstantGivenTime("09:30"), buildInstantGivenTime("10:00"), "Event name"),
                          new RoomEvent(buildInstantGivenTime("10:30"), buildInstantGivenTime("11:45"), "Event name"),
                          new RoomEvent(buildInstantGivenTime("12:30"), buildInstantGivenTime("13:00"), "Event name"),
                          new RoomEvent(buildInstantGivenTime("13:00"), buildInstantGivenTime("16:00"), "Event name"),
                          new RoomEvent(buildInstantGivenTime("16:30"), buildInstantGivenTime("17:30"), "Event name")]

        when:
        def slots = new SlotModel.Companion().build(roomEvents, false)

        then:
        slots.size() == 10

        slots[0].startTime == "T08:00:00Z"
        slots[0].endTime == "T09:30:00Z"

        slots[1].startTime == "T09:30:00Z"
        slots[1].endTime == "T10:00:00Z"

        slots[2].startTime == "T10:00:00Z"
        slots[2].endTime == "T10:30:00Z"

        slots[3].startTime == "T10:30:00Z"
        slots[3].endTime == "T11:45:00Z"

        slots[4].startTime == "T11:45:00Z"
        slots[4].endTime == "T12:30:00Z"

        slots[5].startTime == "T12:30:00Z"
        slots[5].endTime == "T13:00:00Z"

        slots[6].startTime == "T13:00:00Z"
        slots[6].endTime == "T16:00:00Z"

        slots[7].startTime == "T16:00:00Z"
        slots[7].endTime == "T16:30:00Z"

        slots[8].startTime == "T16:30:00Z"
        slots[8].endTime == "T17:30:00Z"

        slots[9].startTime == "T17:30:00Z"
        slots[9].endTime == "T17:54:00Z"

    }

    def "should build a MULTI event slot model with OVERLAPPING events correctly"() {
        given: "Multiple room events "
        def roomEvents = [new RoomEvent(buildInstantGivenTime("08:00"), buildInstantGivenTime("12:00"), "Event name"),
                          new RoomEvent(buildInstantGivenTime("11:30"), buildInstantGivenTime("12:00"), "Event name"),
                          new RoomEvent(buildInstantGivenTime("12:00"), buildInstantGivenTime("15:00"), "Event name"),
                          new RoomEvent(buildInstantGivenTime("14:00"), buildInstantGivenTime("17:00"), "Event name")]

        when:
        def slots = new SlotModel.Companion().build(roomEvents, false)

        then:
        slots.size() == 4

        slots[0].startTime == "T08:00:00Z"
        slots[0].endTime == "T12:00:00Z"

        slots[1].startTime == "T12:00:00Z"
        slots[1].endTime == "T15:00:00Z"

        slots[2].startTime == "T15:00:00Z"
        slots[2].endTime == "T17:00:00Z"

        slots[3].startTime == "T17:00:00Z"
        slots[3].endTime == "T17:54:00Z"

    }


    def buildInstantGivenTime(String time) {
        return Instant.parse(Instant.now().toString().substring(0, 11) + time + ":00Z")
    }
}
