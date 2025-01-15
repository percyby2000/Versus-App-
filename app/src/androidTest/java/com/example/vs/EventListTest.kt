package com.example.vs

import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventListTest {
    @get:Rule
    val activityRule = ActivityTestRule(EventListActivity::class.java)

    private lateinit var db: FirebaseFirestore

    @Before
    suspend fun setup() {
        db = FirebaseFirestore.getInstance()
        // Crear algunos eventos de prueba
        createTestEvents()
    }

    private suspend fun createTestEvents() {
        val event = hashMapOf(
            "id" to "test_${System.currentTimeMillis()}",
            "sportType" to "Fútbol",
            "eventName" to "Partido de prueba",
            "duration" to "2 hours",
            "price" to 50.0,
            "location" to "Campo de prueba"
        )

        db.collection("events").add(event).await()
    }

    @Test
    fun testEventListLoading() {
        // Esperar a que se carguen los eventos
        Thread.sleep(2000)

        // Verificar que el RecyclerView tiene elementos
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recyclerView)
        assert(recyclerView.adapter?.itemCount ?: 0 > 0)
    }
}