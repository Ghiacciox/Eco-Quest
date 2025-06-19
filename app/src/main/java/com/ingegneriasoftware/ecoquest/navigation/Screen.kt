package com.ingegneriasoftware.ecoquest.navigation

/**
 * Classe sigillata che rappresenta le diverse schermate dell'applicazione.
 * Ogni schermata è definita come un oggetto con una route univoca.
 * La route viene utilizzata per identificare e navigare tra le schermate.
 */
sealed class Screen(val route: String) {
    // Schermata per il completamento del profilo.
    object ProfileCompletion : Screen("profileCompletion")

    // Schermata di login.
    object Login : Screen("login")

    // Schermata di registrazione.
    object Signup : Screen("signup")

    // Schermata principale (Home).
    object Home : Screen("Home")

    // Schermata delle missioni.
    object Missions : Screen("Quests")

    // Schermata dei trofei.
    object Trophies : Screen("Trofei")

    // Schermata della classifica.
    object Leaderboard : Screen("Classifica")

    // Schermata del profilo utente.
    object Profile : Screen("Profilo")

    // Schermata della cronologia delle missioni.
    object QuestHistory : Screen("questHistory")

    // Schermata per visualizzare una singola missione.
    object SingleMission : Screen("singleMission")

    // Schermata per visualizzare un singolo trofeo.
    object SingleTrophy : Screen("singleTrophy")

    // Schermata per la missione con fotocamera.
    object CameraMission : Screen("cameraMission")

    // Schermata per modificare il profilo utente.
    object ProfileChange : Screen("profileChange")

    // Schermata per la missione quiz.
    object QuizMission : Screen("QuizMission")

    object Onboarding : Screen("onboarding")
}