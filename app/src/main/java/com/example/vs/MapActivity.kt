package com.example.vs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private var selectedLocation: LatLng? = null
    private var selectedLocationName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<Button>(R.id.confirmLocationButton).setOnClickListener {
            selectedLocation?.let { location ->
                val intent = Intent().apply {
                    putExtra("LOCATION_NAME", selectedLocationName)
                    putExtra("LOCATION_LATLNG", "${location.latitude},${location.longitude}")
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Configurar el mapa
        map.setOnMapLongClickListener { latLng ->
            map.clear()
            selectedLocation = latLng
            selectedLocationName = "Ubicación seleccionada"
            map.addMarker(MarkerOptions().position(latLng).title(selectedLocationName))
        }

        // Centrar el mapa en una ubicación inicial
        val defaultLocation = LatLng(-13.1516, -74.2313)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))
    }
}