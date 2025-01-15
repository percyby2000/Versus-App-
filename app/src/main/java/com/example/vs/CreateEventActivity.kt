package com.example.vs

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vs.databinding.ActivityCreateEventBinding
import com.google.android.datatransport.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class CreateEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateEventBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var sportType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Obtener el tipo de deporte
        sportType = intent.getStringExtra("SPORT_TYPE") ?: ""
        binding.tvSportType.text = "Deporte: $sportType"

        // Configurar el botón de crear evento
        binding.btnCreateEvent.setOnClickListener {
            createEvent()
        }

        // Configurar el botón de ver eventos
        binding.btnViewEvents.setOnClickListener {
            val intent = Intent(this, EventListActivity::class.java)
            intent.putExtra("SPORT_TYPE", sportType)
            startActivity(intent)
        }

        // Configurar el botón de ubicación
        binding.btnSelectLocation.setOnClickListener {
            // Aquí implementaremos la selección de ubicación más adelante
            // Por ahora solo guardamos el texto ingresado
        }
        // Configurar el botón de buscar ubicación
        binding.btnSelectLocation.setOnClickListener {
            Toast.makeText(this, "Ubicación encontrada", Toast.LENGTH_SHORT).show()
        }

        // Configurar el botón de crear evento
        binding.btnCreateEvent.setOnClickListener {
            createEvent()
            Toast.makeText(this, "Evento creado 😊", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createEvent() {
        val eventName = binding.etEventName.text.toString()
        val duration = binding.etDuration.text.toString()
        val price = binding.etPrice.text.toString().toDoubleOrNull() ?: 0.0
        val location = binding.etLocation.text.toString()

        if (eventName.isBlank() || duration.isBlank() || location.isBlank()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val event = Event(
            id = UUID.randomUUID().toString(),
            sportType = sportType,
            eventName = eventName,
            duration = duration,
            price = price,
            location = location,
            creatorId = auth.currentUser?.uid ?: ""
        )

        db.collection("events")
            .document(event.id)
            .set(event)
            .addOnSuccessListener {
                Toast.makeText(this, "Evento creado exitosamente", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al crear el evento: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}