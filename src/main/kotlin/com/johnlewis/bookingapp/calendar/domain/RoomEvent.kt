package com.johnlewis.bookingapp.calendar.domain

import java.time.Instant

data class RoomEvent(val startTime: Instant,
                     val endTime: Instant,
                     val eventName: String)
