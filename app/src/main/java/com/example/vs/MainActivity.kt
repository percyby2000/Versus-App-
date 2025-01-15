package com.example.vs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_main)

        // Referencias a los elementos de la interfaz
        val btnLogin = findViewById<Button>(R.id.btnLogin) // Botón "Iniciar Sesión"
        val emailField = findViewById<TextInputEditText>(R.id.etUsername) // Campo de correo
        val passwordField = findViewById<TextInputEditText>(R.id.etPassword) // Campo de contraseña
        val btnRegister = findViewById<Button>(R.id.btnRegister) // Botón "Crear Cuenta"

        // Botón "Crear Cuenta" redirige a Signup
        btnRegister.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        // Botón "Iniciar Sesión"
        btnLogin.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Intentar iniciar sesión con Firebase
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = mAuth.currentUser
                            // Comprobar si el correo electrónico ha sido verificado
                            if (user != null && user.isEmailVerified) {
                                // Redirigir al menú principal si el inicio de sesión es exitoso
                                Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MenuPrincipal::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // Si el correo no ha sido verificado
                                Toast.makeText(this, "Por favor, verifica tu correo electrónico.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Identificar el tipo de error
                            val errorMessage = when (task.exception) {
                                is FirebaseAuthInvalidUserException -> "Dichos datos no son correctos."
                                is FirebaseAuthInvalidCredentialsException -> "Contraseña incorrecta."
                                else -> task.exception?.message ?: "Error desconocido."
                            }
                            // Mostrar mensaje de error
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Validar que no se dejen campos vacíos
                Toast.makeText(this, "Por favor ingresa el correo y la contraseña.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}