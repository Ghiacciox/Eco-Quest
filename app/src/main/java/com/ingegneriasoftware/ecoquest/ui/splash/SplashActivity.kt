package com.ingegneriasoftware.ecoquest.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ingegneriasoftware.ecoquest.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen() // Mostra lo splash screen

        startActivity(Intent(this, MainActivity::class.java))
        finish() // Chiude l'activity dello splash screen
    }
}