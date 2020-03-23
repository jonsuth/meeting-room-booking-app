package com.johnlewis.bookingapp.calendar.model

import com.johnlewis.bookingapp.calendar.domain.Room
import spock.lang.Specification

class CalendarModelTest extends Specification {

    def "should build a EMPTY model when there are no events"() {
        given:
        def rooms = []

        when:
        def model = new CalendarModel.Companion().build(rooms)

        then:
        model.rooms == []
    }

    def "should build a SORTED model from domain that is by room number"() {
        given:
        def rooms = [new Room("1.2@id.com", "1.2", 1, 2, 4, ["PHONE", "TV"], "", [], true),
                     new Room("1.3@id.com", "1.5", 1, 5, 5, ["PHONE", "TV"], "", [], true),
                     new Room("1.4@id.com", "1.4", 1, 4, 6, ["PHONE", "TV"], "", [], true),
                     new Room("1.5@id.com", "1.3", 1, 3, 1, ["PHONE", "TV"], "", [], true),
                     new Room("1.5@id.com", "2.3", 2, 3, 1, ["PHONE", "TV"], "", [], true),
                     new Room("1.5@id.com", "3.2", 3, 2, 1, ["PHONE", "TV"], "", [], true),
        ]

        when:
        def model = new CalendarModel.Companion().build(rooms)

        then:
        model.rooms != null
        model.rooms[0].name == "1.2"
        model.rooms[1].name == "1.3"
        model.rooms[2].name == "1.4"
        model.rooms[3].name == "1.5"
        model.rooms[4].name == "2.3"
        model.rooms[5].name == "3.2"
    }
}
