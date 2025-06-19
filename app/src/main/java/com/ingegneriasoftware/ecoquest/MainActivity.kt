package com.ingegneriasoftware.ecoquest

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.ingegneriasoftware.ecoquest.data.repositories.AuthRepository
import com.ingegneriasoftware.ecoquest.data.repositories.ProfileRepository
import com.ingegneriasoftware.ecoquest.ui.theme.EcoQuestTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository // Inject AuthRepository

    @Inject
    lateinit var profileRepository: ProfileRepository // Inject ProfileRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EcoQuestTheme {
                // Pass the injected AuthRepository and ProfileRepository to EcoQuestApp
                EcoQuestApp(authRepository = authRepository, profileRepository = profileRepository)
            }
            SetStatusBarColor()
        }
    }

    @Composable
    fun SetStatusBarColor(
        statusBarColor: Color = MaterialTheme.colorScheme.primaryContainer,
        useDarkIcons: Boolean = true
    ) {
        val activity = LocalActivity.current

        SideEffect {
            activity?.let {
                val window = it.window
                val colorInt = statusBarColor.toArgb()

                WindowCompat.setDecorFitsSystemWindows(window, true)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.setSystemBarsAppearance(
                        if (useDarkIcons) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        window.statusBarColor = colorInt
                    }
                } else {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    @Suppress("DEPRECATION")
                    window.statusBarColor = colorInt
                    @Suppress("DEPRECATION")
                    window.decorView.systemUiVisibility = if (useDarkIcons)
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    else
                        0
                }
            }
        }
    }

}