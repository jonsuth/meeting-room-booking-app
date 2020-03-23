package com.johnlewis.bookingapp.calendar.service

import com.johnlewis.bookingapp.google_sheets.GoogleSheetsService
import org.springframework.stereotype.Component

@Component
class SheetsService(googleSheetsService: GoogleSheetsService) {

    private val sheets = googleSheetsService.getSheetsService()
    private val spreadsheetId = "1l7km3wsQrqDYweZzn7GCb4LoemJmABGU3AkDA6fKkp4"
    private val range = "Sheet1!D1:E1"

    fun getRoomMotion(roomID: String): Boolean {
        val response = sheets.spreadsheets()
                .values()
                .get(spreadsheetId, range)
                .execute()

        val values = response.getValues()
        if (values == null || values.size == 0) {
            println("SHEET ERROR - No data was found.")
        } else {
            for (row in values) {
                if (row[0] == (roomID)) {
                    if (row[1] == "Room in use") {
                        return true
                    }
                    return false
                }
            }
        }
        return false
    }
}
