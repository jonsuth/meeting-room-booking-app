package com.johnlewis.bookingapp.google_sheets

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.Collections


@Component
class GoogleSheetsService {
    // TODO Plug in real sheet to read data from
    //WIP
    @Throws(IOException::class)
    fun getSheetsService(): Sheets {
        val credential = buildCredential()
        return Sheets
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build()
    }

    companion object {

        private val APPLICATION_NAME = "Meeting Room Booker"

        private val JSON_FACTORY = JacksonFactory.getDefaultInstance()

        private var HTTP_TRANSPORT: HttpTransport? = GoogleNetHttpTransport.newTrustedTransport()

        @Throws(IOException::class)
        fun buildCredential(): GoogleCredential {
            return GoogleCredential
                    .fromStream(GoogleSheetsService::class.java.getResourceAsStream("/My Project 13154-960ee266a1cc.json"))
                    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS))
        }
    }
}