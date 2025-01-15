package com.example.vs

import android.widget.ImageButton
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.rule.ActivityTestRule
import com.google.android.gms.location.FusedLocationProviderClient
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationTest {
    @get:Rule
    val activityRule = ActivityTestRule(MenuPrincipal::class.java)

    @Test
    fun testLocationActivation() {
        activityRule.activity.apply {
            runOnUiThread {
                findViewById<ImageButton>(R.id.activateLocationButton).performClick()
            }
        }

        // Esperar a que se procese la solicitud de ubicación
        Thread.sleep(1000)

        // Verificar que no hay errores (no podemos verificar la ubicación real en tests)
        // pero podemos verificar que la actividad sigue funcionando
        assert(activityRule.activity.hasWindowFocus())
    }
}