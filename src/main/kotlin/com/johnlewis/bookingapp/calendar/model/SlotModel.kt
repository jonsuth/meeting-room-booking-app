package com.johnlewis.bookingapp.calendar.model

import com.johnlewis.bookingapp.calendar.domain.RoomEvent
import java.time.Instant

data class SlotModel(var movement: Boolean,
                     val width: Double,
                     val available: Boolean,
                     val eventName: String,
                     val startTime: String,
                     val endTime: String) {
    companion object {

        val MILLIS_IN_SLOT = 60000
        val START_OF_DAY = Instant.parse(Instant.now().toString().substring(0, 10) + "T08:00:00.00Z")
        val END_OF_DAY = Instant.parse(Instant.now().toString().substring(0, 10) + "T17:54:00.00Z")

        fun build(roomEvents: List<RoomEvent>, hasMotion: Boolean): List<SlotModel> {
            val slotModel = when {
                roomEvents.isEmpty() -> mutableListOf()
                roomEvents.size == 1 -> buildSingleEventSlotModel(roomEvents.first())
                else -> buildMultiEventSlotModel(roomEvents)
            }

            slotModel.populateMotion(hasMotion)

            return slotModel
        }

        private fun buildSingleEventSlotModel(roomEvent: RoomEvent): MutableList<SlotModel> {
            val slotModel = mutableListOf<SlotModel>()

            if (roomEvent.startTime.isBefore(START_OF_DAY)) {
                slotModel.addBookedEvent(START_OF_DAY, roomEvent.endTime, roomEvent.eventName)
            } else {
                slotModel.addFreeSlotBetweenStartOfDayAndThis(roomEvent)
            }

            if (roomEvent.endTime.isAfter(END_OF_DAY)) {
                slotModel.addBookedEvent(roomEvent.startTime, END_OF_DAY, roomEvent.eventName)
            } else {
                slotModel.addBookedEvent(roomEvent.startTime, roomEvent.endTime, roomEvent.eventName)
                slotModel.addFreeSlotBetweenThisAndEndOfDay(roomEvent, roomEvent.startTime)
            }

            return slotModel
        }

        private fun buildMultiEventSlotModel(roomEvents: List<RoomEvent>): MutableList<SlotModel> {
            val slotModel = mutableListOf<SlotModel>()
            var lastEventEndTime = Instant.parse("2000-01-01T00:00:00.00Z")
            var thisEventEndTime: Instant

            for (roomEvent in roomEvents) {
                if (roomEvent == roomEvents.first()) {
                    slotModel.addFreeSlotBetweenStartOfDayAndThis(roomEvent)
                    slotModel.addBookedEvent(roomEvent.startTime, roomEvent.endTime, roomEvent.eventName)
                } else if (roomEvent.startTime.isAfter(END_OF_DAY)) {
                } else {

                    thisEventEndTime = if (roomEvent.endTime.isBefore(END_OF_DAY)) {
                        roomEvent.endTime
                    } else {
                        END_OF_DAY
                    }

                    when {
                        eventsAreOverlapped(lastEventEndTime, roomEvent.startTime, thisEventEndTime) -> {
                            slotModel.addBookedEvent(lastEventEndTime, thisEventEndTime, roomEvent.eventName)
                        }

                        eventsAreBackToBack(lastEventEndTime, roomEvent) -> {
                            slotModel.addBookedEvent(roomEvent.startTime, thisEventEndTime, roomEvent.eventName)
                        }

                        eventsHaveAGapInBetween(lastEventEndTime, roomEvent) -> {
                            slotModel.addFreeEvent(lastEventEndTime, roomEvent.startTime, roomEvent.eventName)
                            slotModel.addBookedEvent(roomEvent.startTime, thisEventEndTime, roomEvent.eventName)
                        }
                    }

                    if (roomEvent == roomEvents.last()) {
                        slotModel.addFreeSlotBetweenThisAndEndOfDay(roomEvent, lastEventEndTime)
                    }
                }
                if (roomEvent.endTime.isAfter(lastEventEndTime)) {
                    lastEventEndTime = if (roomEvent.endTime.isAfter(END_OF_DAY)) {
                        END_OF_DAY
                    } else {
                        roomEvent.endTime
                    }
                }
            }

            return slotModel
        }

        private fun MutableList<SlotModel>.addFreeSlotBetweenStartOfDayAndThis(roomEvent: RoomEvent) {
            if (roomEvent.startTime.isAfter(SlotModel.START_OF_DAY)) {
                this.addFreeEvent(START_OF_DAY, roomEvent.startTime, roomEvent.eventName)
            }
        }

        private fun MutableList<SlotModel>.addFreeSlotBetweenThisAndEndOfDay(roomEvent: RoomEvent, lastEventEndTime: Instant) {
            if (roomEvent.endTime.isAfter(lastEventEndTime) && roomEvent.endTime.isBefore(SlotModel.END_OF_DAY)) {
                this.addFreeEvent(roomEvent.endTime, END_OF_DAY, roomEvent.eventName)
            } else if (roomEvent.endTime.isBefore(lastEventEndTime)) {
                this.addFreeEvent(lastEventEndTime, END_OF_DAY, roomEvent.eventName)
            }
        }

        private fun MutableList<SlotModel>.addBookedEvent(startTime: Instant, endTime: Instant, eventName: String) {
            this.add(SlotModel(
                    movement = false,
                    width = slotsBetweenTimes(startTime, endTime),
                    available = false,
                    eventName = eventName,
                    startTime = startTime.toTimeString(),
                    endTime = endTime.toTimeString()))
        }

        private fun MutableList<SlotModel>.addFreeEvent(startTime: Instant, endTime: Instant, eventName: String) {
            this.add(SlotModel(
                    movement = false,
                    width = slotsBetweenTimes(startTime, endTime),
                    available = true,
                    eventName = eventName,
                    startTime = startTime.toTimeString(),
                    endTime = endTime.toTimeString()))
        }

        private fun MutableList<SlotModel>.populateMotion(hasMotion: Boolean) {
            this.forEach {
                if (eventIsInCurrentTime(it)) {
                    it.movement = hasMotion
                }
            }
        }

        private fun eventIsInCurrentTime(it: SlotModel): Boolean {
            val currentTime = Instant.now()
            val startTime = Instant.parse(Instant.now().toString().substring(0, 10) + it.startTime)
            val endTime = Instant.parse(Instant.now().toString().substring(0, 10) + it.endTime)

            if (startTime.isBefore(currentTime) && endTime.isAfter(currentTime)) {
                return true
            }
            return false
        }

        private fun eventsAreOverlapped(lastEventEndTime: Instant, thisEventStartTime: Instant, thisEventEndTime: Instant) =
                thisEventStartTime.isBefore(lastEventEndTime) && thisEventEndTime.isAfter(lastEventEndTime)

        private fun eventsAreBackToBack(lastEventEndTime: Instant, roomEvent: RoomEvent) =
                lastEventEndTime == roomEvent.startTime

        private fun eventsHaveAGapInBetween(lastEventEndTime: Instant, roomEvent: RoomEvent) =
                roomEvent.startTime.isAfter(lastEventEndTime)

        private fun slotsBetweenTimes(startTime: Instant, endTime: Instant): Double {
            val lengthOfBookingInMilli = endTime.toEpochMilli().minus(startTime.toEpochMilli())
            return lengthOfBookingInMilli.div(MILLIS_IN_SLOT).toDouble()
        }

        private fun Instant.toTimeString(): String {
            return this.toString().substring(10, this.toString().length)
        }
    }
}
