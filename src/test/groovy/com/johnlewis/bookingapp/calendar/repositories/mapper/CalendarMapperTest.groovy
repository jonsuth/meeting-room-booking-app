package com.johnlewis.bookingapp.calendar.repositories.mapper

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.api.services.calendar.model.CalendarListEntry
import com.johnlewis.bookingapp.calendar.repositories.resources.ListOfValues
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class CalendarMapperTest extends Specification {

    @Autowired
    def lisOfValues = new ListOfValues()

    def calendarMapper = new CalendarMapper(lisOfValues)
    def objectMapper = new ObjectMapper()

    def "should map google calendar list to room domain"() {
        given: "A list of room calendars"
        def calendars = objectMapper.readValue(calendarListJson, new TypeReference<List<CalendarListEntry>>() {})

        when:
        def result = calendarMapper.mapCalendarListToRooms(calendars)

        then:
        result.size() == 3
        result[0].id == "johnlewis.co.uk_343430363239352d393930@resource.calendar.google.com"
        result[0].name == "3.1"
        result[0].floorNumber == 3
        result[0].roomNumber == 1
        result[0].capacity == 6
        result[0].equipment[0] == "PHONE"
        result[0].equipment[1] == "TV"

        result[1].id == "johnlewis.co.uk_2d333731323036322d393030@resource.calendar.google.com"
        result[1].name == "3.2"
        result[1].capacity == 8
        result[1].equipment == ["Information not available"]
    }

    def calendarListJson = """[
  {
    "kind": "calendar#calendarListEntry",
    "etag": "\\"1520100276274000\\"",
    "id": "johnlewis.co.uk_343430363239352d393930@resource.calendar.google.com",
    "summary": "1 Cathedral Piazza-3-OPEN-3.1 (6) [PHONE,TV]",
    "timeZone": "Europe/London",
    "colorId": "1",
    "backgroundColor": "#ac725e",
    "foregroundColor": "#000000",
    "selected": true,
    "accessRole": "reader",
    "defaultReminders": []
  },
  {
    "kind": "calendar#calendarListEntry",
    "etag": "\\"1520100276274000\\"",
    "id": "johnlewis.co.uk_2d333731323036322d393030@resource.calendar.google.com",
    "summary": "1 Cathedral Piazza-3-OPEN-3.2 (8)",
    "timeZone": "Europe/London",
    "colorId": "1",
    "backgroundColor": "#ac725e",
    "foregroundColor": "#000000",
    "selected": true,
    "accessRole": "reader",
    "defaultReminders": []
  },
  {
    "kind": "calendar#calendarListEntry",
    "etag": "\\"1520100276274000\\"",
    "id": "johnlewis.co.uk_3432333137393135313634@resource.calendar.google.com",
    "summary": "1 Cathedral Piazza-3-OPEN-3.3 (10) [PHONE]",
    "timeZone": "Europe/London",
    "colorId": "1",
    "backgroundColor": "#ac725e",
    "foregroundColor": "#000000",
    "selected": true,
    "accessRole": "reader",
    "defaultReminders": []
  }
]
"""
}
