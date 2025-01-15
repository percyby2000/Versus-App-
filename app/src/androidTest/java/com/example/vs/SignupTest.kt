package com.example.vs

import android.widget.Button
import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignupTest {
    @get:Rule
    val activityRule = ActivityTestRule(Signup::class.java)

    private lateinit var auth: FirebaseAuth

    @Before
    fun setup() {
        auth = FirebaseAuth.getInstance()
    }

    @Test
    fun testSuccessfulRegistration() {
        // Generar email único para evitar conflictos
        val timestamp = System.currentTimeMillis()
        val email = "test$timestamp@example.com"
        val password = "123456"
        val name = "Test User"

        activityRule.activity.apply {
            runOnUiThread {
                findViewById<EditText>(R.id.nameInput).setText(name)
                findViewById<EditText>(R.id.emailInput).setText(email)
                findViewById<EditText>(R.id.passwordInput).setText(password)
                findViewById<Button>(R.id.registerButton).performClick()
            }
        }

        // Esperar a que el registro se complete
        Thread.sleep(2000)

        // Verificar que el usuario existe
        val currentUser = auth.currentUser
        assert(currentUser != null)
        assert(currentUser?.email == email)
    }

    @Test
    fun testInvalidPasswordRegistration() {
        val timestamp = System.currentTimeMillis()
        val email = "test$timestamp@example.com"
        val password = "123" // contraseña muy corta
        val name = "Test User"

        activityRule.activity.apply {
            runOnUiThread {
                findViewById<EditText>(R.id.nameInput).setText(name)
                findViewById<EditText>(R.id.emailInput).setText(email)
                findViewById<EditText>(R.id.passwordInput).setText(password)
                findViewById<Button>(R.id.registerButton).performClick()
            }
        }

        // Esperar mensaje de error
        Thread.sleep(1000)

        // El usuario no debería existir
        assert(auth.currentUser == null)
    }
}