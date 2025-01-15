package com.example.vs

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import java.text.SimpleDateFormat
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatabaseInstrumentedTest {

    private lateinit var dbHelper: DatabaseHelper

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dbHelper = DatabaseHelper(context)
    }

    @After
    fun tearDown() {
        dbHelper.close()
    }

    @Test
    fun databaseInsert_isSuccessful() {
        // Crear un reto de prueba
        val challenge = SportsChallenge(
            title = "Test Challenge",
            description = "Test Description",
            sportType = "Running",
            difficulty = "Principiante",
            duration = "7 días",
            goal = "21km",
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            status = "Activo"
        )

        // Insertar en la base de datos
        val id = dbHelper.insertChallenge(challenge)

        // Verificar que la inserción fue exitosa
        assertTrue(id > 0)

        // Obtener todos los retos
        val challenges = dbHelper.getAllChallenges()

        // Verificar que el reto está en la lista
        assertTrue(challenges.any { it.title == "Test Challenge" })
    }

    @Test
    fun databaseQuery_returnsCorrectData() {
        // Insertar datos de prueba
        val challenge1 = SportsChallenge(
            title = "Challenge 1",
            description = "Description 1",
            sportType = "Fútbol",
            difficulty = "Intermedio",
            duration = "14 días",
            goal = "10 partidos",
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            status = "Activo"
        )

        val challenge2 = SportsChallenge(
            title = "Challenge 2",
            description = "Description 2",
            sportType = "Natación",
            difficulty = "Avanzado",
            duration = "30 días",
            goal = "1500m diarios",
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            status = "Activo"
        )

        dbHelper.insertChallenge(challenge1)
        dbHelper.insertChallenge(challenge2)

        // Obtener todos los retos
        val challenges = dbHelper.getAllChallenges()

        // Verificar que hay al menos 2 retos
        assertTrue(challenges.size >= 2)

        // Verificar que los datos son correctos
        val savedChallenge1 = challenges.find { it.title == "Challenge 1" }
        val savedChallenge2 = challenges.find { it.title == "Challenge 2" }

        assertNotNull(savedChallenge1)
        assertNotNull(savedChallenge2)
        assertEquals("Fútbol", savedChallenge1?.sportType)
        assertEquals("Natación", savedChallenge2?.sportType)
    }
}