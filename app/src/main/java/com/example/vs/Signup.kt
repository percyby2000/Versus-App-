package com.example.vs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Signup : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_signup)

        // Obtener referencias a los elementos de la interfaz
        val nameField = findViewById<EditText>(R.id.nameInput)
        val emailField = findViewById<EditText>(R.id.emailInput)
        val passwordField = findViewById<EditText>(R.id.passwordInput)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginLink = findViewById<TextView>(R.id.loginLink)

        // Manejar el evento de clic en el botón "Registrarse"
        registerButton.setOnClickListener {
            val name = nameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                if (password.length >= 6) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Obtener el usuario registrado
                                val user = mAuth.currentUser
                                // Enviar el correo de verificación
                                sendEmailVerification(user)

                                // Mostrar mensaje de éxito
                                Toast.makeText(
                                    this,
                                    "Registro exitoso. ¡Bienvenido, $name! Te hemos enviado un correo de verificación.",
                                    Toast.LENGTH_LONG
                                ).show()

                                // Redirigir a la pantalla principal
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // Mostrar error
                                Toast.makeText(
                                    this,
                                    "Error: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor llena todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }

        // Manejar el evento de clic en ¿Ya tienes cuenta? Inicia sesion
        loginLink.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Metodo para enviar correo de verificación
    private fun sendEmailVerification(user: FirebaseUser?) {
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Correo de verificación enviado.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al enviar correo de verificación.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

