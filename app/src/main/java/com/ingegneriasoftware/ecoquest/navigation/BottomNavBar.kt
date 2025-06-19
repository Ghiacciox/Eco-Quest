package com.ingegneriasoftware.ecoquest.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cottage
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Composable che rappresenta una barra di navigazione inferiore (Bottom Navigation Bar).
 * @param navController Il NavController utilizzato per gestire la navigazione tra le schermate.
 */
@Composable
fun BottomNavBar(navController: NavController) {
    // Definisce le schermate disponibili nella barra di navigazione.
    val items = listOf(
        Screen.Home,
        Screen.Missions,
        Screen.Trophies,
        Screen.Leaderboard,
        Screen.Profile
    )

    // Definisce la forma arrotondata per la parte superiore della barra.
    val shapeTopRounded = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    // Componente principale della barra di navigazione.
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer, // Colore di sfondo della barra.
        modifier = Modifier
            .padding(start = 6.dp, end = 6.dp, bottom = 0.dp, top = 0.dp) // Margini esterni.
            .background(color = MaterialTheme.colorScheme.background, shapeTopRounded) // Sfondo con forma arrotondata.
            .padding(bottom = 16.dp) // Padding interno.
            .clip(RoundedCornerShape(16.dp)) // Clip per arrotondare i bordi.
            .shadow(
                elevation = 8.dp, // Ombra della barra.
                shape = RoundedCornerShape(16.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            )
            .border(
                BorderStroke(
                    width = 1.5.dp, // Larghezza del bordo.
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    )
                ),
                shape = RoundedCornerShape(16.dp) // Forma del bordo.
            )
    ) {
        // Ottiene la schermata corrente dalla pila di navigazione.
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        // Crea un elemento della barra di navigazione per ogni schermata.
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = getIconForRoute(screen.route), // Ottiene l'icona corrispondente alla schermata.
                        contentDescription = screen.route // Descrizione per l'accessibilità.
                    )
                },
                label = {
                    Text(
                        text = screen.route, // Nome della schermata.
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        maxLines = 1, // Limita il testo a una sola riga.
                        softWrap = false // Evita la rottura del testo su più righe.
                    )
                },
                selected = currentRoute == screen.route, // Evidenzia l'elemento selezionato.
                onClick = {
                    navController.navigate(screen.route) {
                        // Torna alla schermata iniziale per evitare una pila di navigazione troppo grande.
                        popUpTo(navController.graph.startDestinationId)
                        // Evita di creare più copie della stessa destinazione.
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.outline, // Colore dell'icona selezionata.
                    selectedTextColor = MaterialTheme.colorScheme.outline, // Colore del testo selezionato.
                    unselectedIconColor = MaterialTheme.colorScheme.primary, // Colore dell'icona non selezionata.
                    unselectedTextColor = MaterialTheme.colorScheme.primary, // Colore del testo non selezionato.
                    indicatorColor = Color.Transparent // Colore dell'indicatore (trasparente).
                )
            )
        }
    }
}

/**
 * Funzione helper per ottenere l'icona corrispondente a una route.
 * @param route La route della schermata.
 * @return L'icona associata alla route.
 */
fun getIconForRoute(route: String): ImageVector {
    return when (route) {
        Screen.Home.route -> Icons.Filled.Cottage
        Screen.Missions.route -> Icons.Filled.Eco
        Screen.Trophies.route -> Icons.Filled.MilitaryTech
        Screen.Leaderboard.route -> Icons.Filled.Leaderboard
        Screen.Profile.route -> Icons.Filled.Face
        else -> Icons.Filled.Cottage // Icona predefinita.
    }
}

