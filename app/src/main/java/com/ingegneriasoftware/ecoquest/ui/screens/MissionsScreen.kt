package com.ingegneriasoftware.ecoquest.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ingegneriasoftware.ecoquest.R
import com.ingegneriasoftware.ecoquest.data.models.CompletedMission
import com.ingegneriasoftware.ecoquest.data.models.Missions
import com.ingegneriasoftware.ecoquest.navigation.Screen
import com.ingegneriasoftware.ecoquest.ui.components.parseDate

import com.ingegneriasoftware.ecoquest.viewmodels.MissionsViewModel

@Composable
fun MissionsScreen(
    navController: NavController,
    viewModel: MissionsViewModel = hiltViewModel()
) {
    val uncompletedMissions by viewModel.uncompletedMissions.collectAsState()
    val completedMissions by viewModel.completedMissions.collectAsState()
    val refreshTrigger by viewModel.refreshTrigger.collectAsState()

    // Gestisce il refresh quando torni indietro da SingleMissionScreen
    LaunchedEffect(refreshTrigger) {
        viewModel.loadMissions()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),                            
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Quest da completare",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { navController.navigate(Screen.QuestHistory.route) },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = "Quest History",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.width(48.dp))

        // Uncompleted Missions Section
        Spacer(modifier = Modifier.height(8.dp))
        if (uncompletedMissions.isEmpty()) {
            Text("Nessuna quest da completare!", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn {
                items(uncompletedMissions) { mission ->
                    MissionItem(
                        mission = mission,
                        onClick = {
                            if (mission.camera) {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "mission",
                                    mission to false
                                )
                                navController.navigate("CameraMission")
                            } else {
                                if(mission.missionPrompt.isNullOrEmpty()) {
                                    viewModel.completeMission(mission.id, mission.points.toInt())
                                }else{
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "mission",
                                        mission to true
                                    )
                                    navController.navigate("QuizMission")
                                }
                            }
                        },
                        onItemClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "mission",
                                mission to false
                            )
                            navController.navigate(Screen.SingleMission.route)
                        }
                    )
                }
                item{
                    Spacer(Modifier.height(100.dp))
                }
            }
        }

        // Completed Missions Section
        Spacer(modifier = Modifier.height(16.dp))
        Text("Completed Missions", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        if (completedMissions.isEmpty()) {
            Text("No completed missions found", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn {
                items(completedMissions) { completedMission ->
                    CompletedMissionItem(
                        completedMission = completedMission,
                        onItemClick = {
                            val mission = Missions(
                                id = completedMission.id,
                                title = completedMission.title,
                                description = completedMission.description,
                                points = completedMission.points,
                                type = completedMission.type,
                                missionPic = completedMission.missionPic,
                                missionPrompt = null,
                                camera = false
                            )
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "mission",
                                mission to true
                            )
                            navController.navigate(Screen.SingleMission.route)
                        }
                    )
                }
                item{
                    Spacer(Modifier.height(100.dp))
                }
            }
        }
    }
}



@Composable
fun MissionItem(
    mission: Missions,
    onClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick() },
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
                )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (!mission.missionPic.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(mission.missionPic),
                    contentDescription = "Mission Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.20f)
                        .clip(RoundedCornerShape(7.dp))
                        .aspectRatio(1f)

                )
            } else {
                // Fallback se missionPic è null o vuota
                Image(
                    painter = painterResource(R.drawable.ic_placeholder),
                    contentDescription = "Mission Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 1/3f)
                        .clip(RoundedCornerShape(6.dp))
                        .aspectRatio(1f)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = mission.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Punti: ${mission.points}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = mission.description ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.outline)
            ) {
            Text( "Completa" )
            }
        }
        }
    }



@Composable
fun CompletedMissionItem(
    completedMission: CompletedMission,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // occuperà tutta la larghezza disponibile
            .padding(horizontal = 16.dp, vertical = 8.dp) // padding esterno ridotto
            .clickable { onItemClick() },
        shape = RoundedCornerShape(15.dp),// bordidella card
        elevation = CardDefaults.cardElevation(4.dp),   // elevazione più bassa
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (!completedMission.missionPic.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(completedMission.missionPic),
                    contentDescription = "Mission Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.20f)
                        .clip(RoundedCornerShape(7.dp))
                        .aspectRatio(1f)

                )
            } else {
                // Fallback se missionPic è null o vuota
                Image(
                    painter = painterResource(R.drawable.ic_placeholder),
                    contentDescription = "Mission Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 1/3f)
                        .clip(RoundedCornerShape(6.dp))
                        .aspectRatio(1f)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = completedMission.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Punti: ${completedMission.points}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = completedMission.description ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text ="Completed on: ${parseDate(completedMission.completedAt.toString())}",
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}


