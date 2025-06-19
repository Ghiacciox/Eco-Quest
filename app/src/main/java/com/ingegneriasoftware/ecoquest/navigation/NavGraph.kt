package com.ingegneriasoftware.ecoquest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.ingegneriasoftware.ecoquest.ui.screens.*

/**
 * Definisce la struttura di navigazione dell'applicazione utilizzando il NavHost di Jetpack Compose.
 * @param navController Il NavHostController utilizzato per gestire la navigazione tra le schermate.
 */
@Composable
fun NavGraph(
    navController: NavHostController
) {
    // Configura il NavHost con la destinazione iniziale e le route disponibili.
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route // La schermata iniziale è la schermata di login.
    ) {
        // Schermata per il completamento del profilo.
        composable(Screen.ProfileCompletion.route) {
            ProfileCompletionScreen(navController)
        }

        // Schermata di login.
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        // Schermata di registrazione.
        composable(Screen.Signup.route) {
            SignupScreen(navController)
        }

        // Schermata principale (Home).
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        // Schermata delle missioni.
        composable(Screen.Missions.route) {
            MissionsScreen(navController)
        }

        // Schermata dei trofei.
        composable(Screen.Trophies.route) {
            TrophiesScreen(navController)
        }

        // Schermata della classifica.
        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(navController)
        }

        // Schermata del profilo utente.
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        // Schermata della cronologia delle missioni.
        composable(Screen.QuestHistory.route) {
            QuestHistoryScreen(navController)
        }

        // Schermata per visualizzare una singola missione.
        composable(Screen.SingleMission.route) {
            SingleMissionScreen(navController)
        }

        // Schermata per visualizzare un singolo trofeo.
        composable(Screen.SingleTrophy.route) {
            SingleTrophyScreen(navController)
        }

        // Schermata per la missione con fotocamera.
        composable(Screen.CameraMission.route) {
            CameraMission(navController)
        }

        // Schermata per modificare il profilo utente.
        composable(Screen.ProfileChange.route) {
            ProfileChangeScreen(navController)
        }

        // Schermata per la missione quiz.
        composable(Screen.QuizMission.route) {
            QuizMission(navController)
        }
        composable(Screen.Onboarding.route) {
           OnboardingScreen(navController)
        }


    }
}