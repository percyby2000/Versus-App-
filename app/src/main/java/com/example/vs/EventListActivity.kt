package com.example.vs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vs.databinding.ActivityEventListBinding
import com.google.firebase.firestore.FirebaseFirestore

class EventListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventListBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var eventsAdapter: EventsAdapter
    private var sportType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        sportType = intent.getStringExtra("SPORT_TYPE") ?: ""
        title = "Eventos de $sportType"

        setupRecyclerView()
        loadEvents()
    }

    private fun setupRecyclerView() {
        eventsAdapter = EventsAdapter { event ->
            val uri = "geo:0,0?q=${event.location}"
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@EventListActivity)
            adapter = eventsAdapter
        }
    }

    private fun loadEvents() {
        db.collection("events")
            .whereEqualTo("sportType", sportType)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, "Error al cargar eventos", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val eventsList = snapshot?.toObjects(Event::class.java) ?: emptyList()
                eventsAdapter.submitList(eventsList)
            }
    }
}