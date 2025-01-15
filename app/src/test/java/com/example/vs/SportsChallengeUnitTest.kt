package com.example.vs

import org.junit.Test
import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

class SportsChallengeUnitTest {

    @Test
    fun challenge_isCorrectlyFormatted() {
        val challenge = SportsChallenge(
            id = 1,
            title = "30 días de running",
            description = "Corre 5km diarios",
            sportType = "Running",
            difficulty = "Intermedio",
            duration = "30 días",
            goal = "150km en total",
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            status = "Activo"
        )

        assertEquals("30 días de running", challenge.title)
        assertEquals("Running", challenge.sportType)
        assertTrue(challenge.duration.contains("días"))
    }

    @Test
    fun challenge_invalidDataThrowsException() {
        var exception = false
        try {
            SportsChallenge(
                id = -1, // ID inválido
                title = "",
                description = "",
                sportType = "",
                difficulty = "",
                duration = "",
                goal = "",
                date = "",
                status = ""
            )
        } catch (e: IllegalArgumentException) {
            exception = true
        }
        assertTrue(exception)
    }
}