package com.johnlewis.bookingapp.google_calendar

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.sheets.v4.SheetsScopes
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.InputStreamReader
import java.util.Arrays


@Component
class GoogleCalendarService {

    @Throws(IOException::class)
    fun getCalendarService(): Calendar {
        val credential = buildCredential()
        return Calendar
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build()
    }

    companion object {

        private val APPLICATION_NAME = "Meeting Room Booker"
        private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
        private var HTTP_TRANSPORT: HttpTransport? = GoogleNetHttpTransport.newTrustedTransport()
        private val SCOPES = Arrays.asList(CalendarScopes.CALENDAR, SheetsScopes.SPREADSHEETS)
        private var DATA_STORE_FACTORY = FileDataStoreFactory(java.io.File(
                System.getProperty("user.home"), ".credentials/jl-meeting-room-booker"))

        @Throws(IOException::class)
        fun buildCredential(): Credential { //GoogleCredential for server, Credential for user
            val input = GoogleCalendarService::class.java.getResourceAsStream("/client_secret.json")
            val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(input))

            // Build flow and trigger user authorization request.
            val flow = GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(DATA_STORE_FACTORY)
                    .setAccessType("offline")
                    .build()
            return AuthorizationCodeInstalledApp(
                    flow, LocalServerReceiver()).authorize("user")
//          return GoogleCredential
//                    .fromStream(GoogleCalendarService::class.java.getResourceAsStream("/My Project 13154-960ee266a1cc.json"))
//                    .createScoped(Collections.singleton(CalendarScopes.CALENDAR_READONLY))
        }
    }
}