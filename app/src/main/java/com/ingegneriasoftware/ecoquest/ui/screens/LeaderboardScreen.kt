package com.ingegneriasoftware.ecoquest.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ingegneriasoftware.ecoquest.ui.components.CustomTabRow
import com.ingegneriasoftware.ecoquest.viewmodels.LeaderboardViewModel
import com.ingegneriasoftware.ecoquest.ui.components.LeaderboardEntryItem

@Composable
fun LeaderboardScreen(
    navController: NavController,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    var selectedPeriod by remember { mutableStateOf("daily") }

    val topUsers by viewModel.topUsers.collectAsState()
    val surroundingUsers by viewModel.surroundingUsers.collectAsState()

    LaunchedEffect(selectedPeriod) {
        viewModel.loadLeaderboard(selectedPeriod)
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Giornaliera", "Settimanale", "Mensile")


   LazyColumn(modifier = Modifier.padding(16.dp)) {
       item{
           CustomTabRow(
               tabs = tabs,
               selectedTabIndex = selectedTabIndex,
               onTabSelected = { selectedTabIndex = it }
           )

       }
       item{
           when (selectedTabIndex) {
               0 -> {
                   selectedPeriod = "daily"
                   Crossfade(targetState = selectedPeriod) {
                       viewModel.loadLeaderboard(it)
                   }
               }
               1 -> {
                   selectedPeriod = "weekly"
                   Crossfade(targetState = selectedPeriod) {
                       viewModel.loadLeaderboard(it)
                   }
               }
               2 -> {
                   selectedPeriod = "monthly"
                   Crossfade(targetState = selectedPeriod) {
                       viewModel.loadLeaderboard(it)
                   }
               }
           }
       }


       item{
           Spacer(modifier = Modifier.height(16.dp))
           Text("Migliori Eco Warriors",
               style = MaterialTheme.typography.displaySmall,
               fontWeight = FontWeight.Bold,
               color = MaterialTheme.colorScheme.tertiary,
               modifier = Modifier
                   .padding(16.dp)
           )
           Spacer(modifier = Modifier.height(8.dp))
       }

       if (topUsers.isEmpty()) {
           item{
               Text("No top users found")
           }
       } else {
           items(topUsers) { user ->
               LeaderboardEntryItem(
                   user = user
               )
           }
       }

       item{
           Spacer(modifier = Modifier.height(16.dp))
           Text(
               "La tua posizione",
               style = MaterialTheme.typography.displaySmall,
               fontWeight = FontWeight.Bold,
               color = MaterialTheme.colorScheme.tertiary,
               modifier = Modifier
                   .padding(16.dp)
           )
           Spacer(modifier = Modifier.height(8.dp))
       }

       if (surroundingUsers.isEmpty()) {
           item {
               Text("No surrounding users found")
           }
       } else {
           items(surroundingUsers) { user ->
               LeaderboardEntryItem(
                   user = user
               )
           }
       }
       item{
           Spacer(modifier = Modifier.height((LocalConfiguration.current.screenHeightDp / 8).dp))
       }
   }
}



