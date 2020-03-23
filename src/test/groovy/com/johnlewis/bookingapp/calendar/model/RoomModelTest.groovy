package com.johnlewis.bookingapp.calendar.model

import com.johnlewis.bookingapp.calendar.domain.Room
import spock.lang.Specification

class RoomModelTest extends Specification {

    def "should build room model from domain"() {
        given:
        def room = new Room("1.2@id.com", "1.2", 1, 2, 4, ["PHONE", "TV"], "", [], true)

        when:
        def model = new RoomModel.Companion().build(room)

        then:
        model.name == "1.2"
        model.floorNumber == 1
        model.roomNumber == 2
        model.id == "1.2@id.com"
        model.slots == []
        model.capacity == "4"
        model.equipment[0] == "PHONE"
        model.equipment[1] == "TV"

    }
}
