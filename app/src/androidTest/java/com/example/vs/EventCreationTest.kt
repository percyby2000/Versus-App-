package com.example.vs

import android.widget.Button
import android.widget.EditText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventCreationTest {
    @get:Rule
    val activityRule = ActivityTestRule(CreateEventActivity::class.java)

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    @Before
    suspend fun setup() {
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        // Asegurarse de que hay un usuario logueado
        if (auth.currentUser == null) {
            auth.signInAnonymously().await()
        }
    }

    @Test
    suspend fun testEventCreation() {
        val eventName = "Test Event ${System.currentTimeMillis()}"
        val duration = "2 hours"
        val price = "50.0"
        val location = "Test Location"

        activityRule.activity.apply {
            runOnUiThread {
                findViewById<EditText>(R.id.etEventName).setText(eventName)
                findViewById<EditText>(R.id.etDuration).setText(duration)
                findViewById<EditText>(R.id.etPrice).setText(price)
                findViewById<EditText>(R.id.etLocation).setText(location)
                findViewById<Button>(R.id.btnCreateEvent).performClick()
            }
        }

        // Esperar a que se cree el evento
        Thread.sleep(2000)

        // Verificar que el evento existe en Firestore
        val events = db.collection("events")
            .whereEqualTo("eventName", eventName)
            .get()
            .await()

        assert(!events.isEmpty)
        val event = events.documents.first()
        assert(event.getString("eventName") == eventName)
        assert(event.getString("duration") == duration)
        assert(event.getDouble("price") == price.toDouble())
    }
}