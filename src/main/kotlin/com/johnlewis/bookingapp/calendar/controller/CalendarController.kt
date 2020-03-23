package com.johnlewis.bookingapp.calendar.controller

import com.johnlewis.bookingapp.calendar.domain.RoomEventRequest
import com.johnlewis.bookingapp.calendar.model.CalendarModel
import com.johnlewis.bookingapp.calendar.model.RoomModel
import com.johnlewis.bookingapp.calendar.repositories.CalendarRepository
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class CalendarController(private val calendarRepository: CalendarRepository) {

    @RequestMapping("/book")
    fun post(@RequestBody roomEventRequest: RoomEventRequest, model: Model): ResponseEntity<Any> {

        calendarRepository.createRoomEvent(roomEventRequest)

        return ResponseEntity(OK)
    }

    @RequestMapping("/dashboard")
    fun get(@RequestParam(required = false) id: String?, model: Model): String {
        return if (id != null) {
            buildRoomView(id, model)
        } else {
            buildDashboardView(model)
        }
    }

    private fun buildDashboardView(model: Model): String {
        val rooms = calendarRepository.getAllRoomCalendars()
        rooms.map {
            it.events = calendarRepository.getEventsForRoomCalendar(it.id)
            if (it.name == "1.9") {
                it.hasMotion = !calendarRepository.getRoomMotion(it.name)
            }
        }

        val calendarModel = CalendarModel.build(rooms)

        model.addAttribute("rooms", calendarModel.rooms)

        return "dashboard"
    }

    private fun buildRoomView(id: String, model: Model): String {
        val room = calendarRepository.getRoomCalendar(id)
        room.events = calendarRepository.getEventsForRoomCalendar(id)
        room.hasMotion = !calendarRepository.getRoomMotion(room.name)

        val roomModel = RoomModel.build(room)

        model.addAttribute("room", roomModel)
        return "room_details"
    }

}