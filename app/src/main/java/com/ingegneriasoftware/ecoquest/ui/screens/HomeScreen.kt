package com.ingegneriasoftware.ecoquest.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ingegneriasoftware.ecoquest.data.models.Missions
import com.ingegneriasoftware.ecoquest.ui.components.Points_card
import com.ingegneriasoftware.ecoquest.ui.components.Tabs_ladder
import com.ingegneriasoftware.ecoquest.ui.components.Tabs_missions
import com.ingegneriasoftware.ecoquest.ui.theme.EcoQuestTheme
import com.ingegneriasoftware.ecoquest.viewmodels.HomeViewModel
import com.ingegneriasoftware.ecoquest.ui.components.TrophySection


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(), // Inject HomeViewModel using Hilt
) {
    // Load data when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }
   EcoQuestTheme {
       Home(viewModel,navController)
   }
}


@Composable
fun Home( viewModel: HomeViewModel = hiltViewModel(),
          navController: NavController) {

    // Collect StateFlow values
    val ecoPoints by viewModel.ecoPoints.collectAsState() //int
    val activeMissions by viewModel.activeMissions.collectAsState() //List<Missions>
    val recentTrophies by viewModel.recentTrophies.collectAsState() //List<AchievedTrophy>
    val leaderboardSummaryDaily by viewModel.leaderboardSummaryDaily.collectAsState()
    val leaderboardSummaryWeekly by viewModel.leaderboardSummaryWeekly.collectAsState()
    val leaderboardSummaryMonthly by viewModel.leaderboardSummaryMonthly.collectAsState()
    val profilePic by viewModel.profilePic.collectAsState()
    val username by viewModel.username.collectAsState()

    val constPadding = 10.dp
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = (LocalConfiguration.current.screenHeightDp * 0.1f).dp),
            horizontalAlignment = Alignment.Start
        ) {
            // ECO-Points Section
            item {
                Spacer(modifier =  Modifier.padding(constPadding))
                Points_card(ecoPoints?.toInt(), profilePic, username)
                Spacer(modifier = Modifier.height(constPadding))
            }
            // Active Missions Section
            item {
                Text(
                    "Quest Attive",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .padding(constPadding)
                )
                Spacer(modifier = Modifier.padding(constPadding))
                MissionSection(activeMissions, navController)


            }

            // Recent Trophies Section
            item {
                Text(
                    "Trofei recenti",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .padding(constPadding)
                )

                Spacer(modifier = Modifier.height(constPadding))
                Spacer(modifier = Modifier.height(constPadding))

                TrophySection(recentTrophies, navController)

                Spacer(modifier = Modifier.height(constPadding))
            }

            // Leaderboard Section
            item {
                Text(
                    "Riassunto classifica",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .padding(constPadding),
                )
                Spacer(modifier = Modifier.height(constPadding))
                Tabs_ladder(
                    leaderboardSummaryDaily,
                    leaderboardSummaryWeekly,
                    leaderboardSummaryMonthly
                )
                Spacer(modifier = Modifier.height(constPadding))
            }
        }
    }
}


@Composable
private fun MissionSection(activeMissions: List<Missions>, navController: NavController) {
    Column {
        if (activeMissions.isEmpty()) {
            Text("Non hai missioni disponibili! " , style = MaterialTheme.typography.bodyMedium)
        } else {
            val giornaliera = activeMissions.filter { it.type == "daily" }
            val settimanale = activeMissions.filter { it.type == "weekly" }
            val mensile = activeMissions.filter { it.type == "monthly" }
            Tabs_missions(
                giornaliera,
                settimanale,
                mensile,
                navController
            )
        }
    }
}




