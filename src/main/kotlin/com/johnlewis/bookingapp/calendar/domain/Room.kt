package com.johnlewis.bookingapp.calendar.domain

data class Room(val id: String,
                val name: String,
                val floorNumber: Int,
                val roomNumber: Int,
                val capacity: Int,
                val equipment: List<String>,
                val location: String?,
                var events: List<RoomEvent> = listOf(),
                var hasMotion: Boolean = false)
