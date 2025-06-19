package com.ingegneriasoftware.ecoquest

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ingegneriasoftware.ecoquest.data.repositories.AuthRepository
import com.ingegneriasoftware.ecoquest.data.repositories.ProfileRepository
import com.ingegneriasoftware.ecoquest.navigation.NavGraph
import com.ingegneriasoftware.ecoquest.navigation.BottomNavBar
import com.ingegneriasoftware.ecoquest.navigation.Screen

@Composable
fun EcoQuestApp(authRepository: AuthRepository, profileRepository: ProfileRepository) {
    val navController = rememberNavController()

    // Observe the authentication state
    val isLoggedIn = authRepository.isLoggedIn.collectAsState().value

    // Store navigation decision to avoid infinite loops
    var hasNavigated by remember { mutableStateOf(false) }

    // Reset hasNavigated when the user logs out
    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            hasNavigated = false // Reset the navigation state
        }
    }

    // Trigger navigation only once when the login state changes
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn && !hasNavigated) {
            val hasProfile = profileRepository.checkProfile()

            val destination = if (hasProfile) Screen.Home.route else Screen.ProfileCompletion.route

            navController.navigate(destination) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }

            hasNavigated = true // Prevents re-triggering navigation
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {

            // Pass both authRepository and profileRepository to NavGraph
        Box(
            contentAlignment = Alignment.BottomEnd,
        ) {
            NavGraph(
                navController = navController,
        )


        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        if (currentRoute in listOf(
                Screen.Home.route,
                Screen.Missions.route,
                Screen.Trophies.route,
                Screen.Leaderboard.route,
                Screen.Profile.route,
                Screen.QuestHistory.route,
                Screen.ProfileChange.route,
                Screen.SingleMission.route
            )
        ) {
                BottomNavBar(navController = navController)
            }
        }
    }
}