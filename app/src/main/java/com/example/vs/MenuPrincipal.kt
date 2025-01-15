package com.example.vs

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MenuPrincipal : AppCompatActivity() {
    private lateinit var logoutButton: ImageButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userNameText: TextView
    private lateinit var userProfileImage: ImageView
    private lateinit var db: FirebaseFirestore

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val REQUEST_CHECK_SETTINGS = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)

        val activateLocationButton: ImageButton = findViewById(R.id.activateLocationButton)
        activateLocationButton.setOnClickListener {
            checkLocationPermission()
        }

        // Inicializaciones
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Referencias UI
        logoutButton = findViewById(R.id.logoutButton)
        userNameText = findViewById(R.id.userNameText)
        userProfileImage = findViewById(R.id.imageView2)

        // Cargar datos del usuario
        loadUserData()

        // Configurar logout
        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Edge-to-Edge config
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupCategoryButtons()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            enableLocation()
        }
    }

    private fun enableLocation() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000 // 10 segundos
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            Toast.makeText(this, "Ubicación activada", Toast.LENGTH_SHORT).show()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignorar el error
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocation()
                } else {
                    Toast.makeText(
                        this,
                        "Permiso de ubicación denegado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Ubicación activada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadUserData() {
        val currentUser = firebaseAuth.currentUser

        currentUser?.let { user ->
            userNameText.text = user.email

            db.collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        document.getString("name")?.let { name ->
                            userNameText.text = name
                        }
                    }
                }
        }
    }

    private fun setupCategoryButtons() {
        val categories = mapOf(
            R.id.category1 to "Fútbol",
            R.id.category2 to "Natación",
            R.id.category3 to "Senderismo",
            R.id.category4 to "Motocross",
            R.id.category5 to "Downhill",
            R.id.category6 to "Kayaking"
        )

        categories.forEach { (buttonId, sportType) ->
            findViewById<ImageButton>(buttonId).setOnClickListener {
                val intent = Intent(this, CreateEventActivity::class.java)
                intent.putExtra("SPORT_TYPE", sportType)
                startActivity(intent)
            }
        }
    }
}