package com.johnlewis.bookingapp.calendar.domain

class RoomEventRequest(val eventName: String? = null,
                       val startDate: String? = null,
                       val endDate: String? = null,
                       val roomId: String? = null,
                       val creatorEmail: String? = null)
